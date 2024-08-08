package com.shreyanshsinghks.shoppingappadmin.presentation.screens.addProduct

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreyanshsinghks.shoppingappadmin.common.ResultState
import com.shreyanshsinghks.shoppingappadmin.domain.model.CategoryModel
import com.shreyanshsinghks.shoppingappadmin.domain.model.ProductModel
import com.shreyanshsinghks.shoppingappadmin.domain.repository.ShoppingAppRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(private val shoppingAppRepo: ShoppingAppRepo) :
    ViewModel() {
    private val _allCategories =
        MutableStateFlow<ResultState<List<CategoryModel>>>(ResultState.Loading)
    val allCategories = _allCategories.asStateFlow()

    private val _productAdded = MutableStateFlow(AddProductUiState())
    val productAdded: StateFlow<AddProductUiState> = _productAdded.asStateFlow()

    init {
        viewModelScope.launch {
            shoppingAppRepo.getCategories().collect {
                _allCategories.value = it
            }
        }
    }


    fun addProduct(product: ProductModel, uri: Uri) {
        viewModelScope.launch {
            shoppingAppRepo.addProduct(product = product, uri = uri).collect {
                when (it) {
                    is ResultState.Error -> {
                        _productAdded.value = _productAdded.value.copy(error = it.message, isLoading = false)
                    }

                    ResultState.Loading -> {
                        _productAdded.value = _productAdded.value.copy(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _productAdded.value = _productAdded.value.copy(success = it.data, isLoading = false)
                    }
                }
            }
        }
    }
}

data class AddProductUiState(
    val isLoading: Boolean = false,
    val error: String? = "",
    val success: String? = "",
)