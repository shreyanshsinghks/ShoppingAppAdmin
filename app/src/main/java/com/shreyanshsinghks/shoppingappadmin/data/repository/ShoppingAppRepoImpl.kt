package com.shreyanshsinghks.shoppingappadmin.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.shreyanshsinghks.shoppingappadmin.common.ResultState
import com.shreyanshsinghks.shoppingappadmin.domain.model.CategoryModel
import com.shreyanshsinghks.shoppingappadmin.domain.repository.ShoppingAppRepo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ShoppingAppRepoImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore) :
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
}