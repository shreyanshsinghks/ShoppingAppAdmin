package com.shreyanshsinghks.shoppingappadmin.domain.repository

import android.net.Uri
import com.shreyanshsinghks.shoppingappadmin.common.ResultState
import com.shreyanshsinghks.shoppingappadmin.domain.model.CategoryModel
import com.shreyanshsinghks.shoppingappadmin.domain.model.ProductModel
import kotlinx.coroutines.flow.Flow

interface ShoppingAppRepo {

    suspend fun addCategory(category: CategoryModel): Flow<ResultState<String>>

    suspend fun getCategories(): Flow<ResultState<List<CategoryModel>>>

    suspend fun addProduct(product: ProductModel, uri: Uri): Flow<ResultState<String>>

}