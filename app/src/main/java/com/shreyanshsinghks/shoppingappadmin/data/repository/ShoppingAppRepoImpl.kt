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

    override suspend fun addImage(uri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseStorage.reference.child("productImages/${System.currentTimeMillis()}").putFile(uri).addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result.storage.downloadUrl.addOnSuccessListener { image ->
                        trySend(ResultState.Success(image.toString()))
                    }
                } else {
                    trySend(ResultState.Error(it.exception?.message.toString()))
                }
            }
        awaitClose {
            close()
        }
    }

    override suspend fun addProduct(product: ProductModel): Flow<ResultState<String>> {
        TODO("Not yet implemented")
    }
}