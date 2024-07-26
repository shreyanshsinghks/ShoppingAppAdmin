package com.shreyanshsinghks.shoppingappadmin.domain.repository

import com.shreyanshsinghks.shoppingappadmin.common.ResultState
import com.shreyanshsinghks.shoppingappadmin.domain.model.CategoryModel
import kotlinx.coroutines.flow.Flow

interface ShoppingAppRepo {

    suspend fun addCategory(category: CategoryModel): Flow<ResultState<String>>

    suspend fun getCategories(): Flow<ResultState<List<CategoryModel>>>

}