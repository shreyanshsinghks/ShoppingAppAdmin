package com.shreyanshsinghks.shoppingappadmin.presentation.screens.addCategory

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreyanshsinghks.shoppingappadmin.common.ResultState
import com.shreyanshsinghks.shoppingappadmin.domain.model.CategoryModel
import com.shreyanshsinghks.shoppingappadmin.domain.repository.ShoppingAppRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(private val shoppingAppRepo: ShoppingAppRepo) : ViewModel() {

    var category by mutableStateOf(CategoryModel())
        private set

    private val _categoryState = MutableStateFlow(CategoryState())
    val categoryState: StateFlow<CategoryState> = _categoryState

    fun updateCategory(newCategory: String) {
        category = category.copy(name = newCategory, createdBy = "Shreyansh")
    }

    fun addCategory() {
        viewModelScope.launch {
            shoppingAppRepo.addCategory(category = category).collect {
                when (it) {
                    is ResultState.Success -> {
                        _categoryState.value = CategoryState(data = it.data, isSuccess = true)
                        category = CategoryModel()
                    }

                    is ResultState.Error -> {
                        _categoryState.value = CategoryState(error = it.message, isSuccess = false)
                        category = CategoryModel()
                    }

                    is ResultState.Loading -> {
                        _categoryState.value = CategoryState(isLoading = true, isSuccess = false)
                        category = CategoryModel()
                    }
                }
            }
        }
    }

}


data class CategoryState(
    val data: String = "",
    val isLoading: Boolean = false,
    val error: String = "",
    val isSuccess: Boolean = false
)