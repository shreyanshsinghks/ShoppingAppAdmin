package com.shreyanshsinghks.shoppingappadmin.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.shreyanshsinghks.shoppingappadmin.common.ResultState
import com.shreyanshsinghks.shoppingappadmin.domain.model.CategoryModel
import com.shreyanshsinghks.shoppingappadmin.domain.model.ProductModel
import com.shreyanshsinghks.shoppingappadmin.domain.repository.ShoppingAppRepo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ShoppingAppRepoImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) :
    ShoppingAppRepo {
    override suspend fun addCategory(category: CategoryModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection("categories").add(category).addOnSuccessListener {
                trySend(ResultState.Success("Category Added"))
            }
                .addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }
            awaitClose {
                close()
            }
        }

    override suspend fun getCategories(): Flow<ResultState<List<CategoryModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        val listenerRegistration = firebaseFirestore.collection("categories")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ResultState.Error(error.message.toString()))
                }

                if (snapshot != null) {
                    val categories = snapshot.toObjects(CategoryModel::class.java)
                    trySend(ResultState.Success(categories))
                }
            }
        awaitClose {
            listenerRegistration.remove()
        }
    }


    override suspend fun addProduct(product: ProductModel, uri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        // Add the product to the Firestore database
        firebaseFirestore.collection("products").add(product)
            .addOnSuccessListener { documentReference ->
                // After the product is added, upload the image to the storage
                firebaseStorage.reference.child("productImages/${documentReference.id}")
                    .putFile(uri)
                    .addOnCompleteListener { uploadTask ->
                        if (uploadTask.isSuccessful) {
                            uploadTask.result.storage.downloadUrl.addOnSuccessListener { imageUrl ->
                                // Update the product with the image URL
                                firebaseFirestore.collection("products").document(documentReference.id)
                                    .update("imageUrl", imageUrl.toString())
                                    .addOnSuccessListener {
                                        trySend(ResultState.Success("Product Added"))
                                    }
                                    .addOnFailureListener { exception ->
                                        trySend(ResultState.Error(exception.message.toString()))
                                    }
                            }
                        } else {
                            trySend(ResultState.Error(uploadTask.exception?.message.toString()))
                        }
                    }
            }
            .addOnFailureListener { exception ->
                trySend(ResultState.Error(exception.message.toString()))
            }

        awaitClose {
            close()
        }
    }

}