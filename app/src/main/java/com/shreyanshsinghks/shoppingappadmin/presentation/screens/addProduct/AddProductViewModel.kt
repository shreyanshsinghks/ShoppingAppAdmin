package com.shreyanshsinghks.shoppingappadmin.presentation.screens.addProduct

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.shreyanshsinghks.shoppingappadmin.common.ResultState
import com.shreyanshsinghks.shoppingappadmin.domain.model.CategoryModel
import com.shreyanshsinghks.shoppingappadmin.domain.repository.ShoppingAppRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(private val shoppingAppRepo: ShoppingAppRepo): ViewModel() {
    private val _allCategories = MutableStateFlow<ResultState<List<CategoryModel>>>(ResultState.Loading)
    val allCategories = _allCategories.asStateFlow()

    init {
        viewModelScope.launch {
            shoppingAppRepo.getCategories().collect {
                _allCategories.value = it
            }
        }
    }

    fun addImage(uri: Uri) {
        viewModelScope.launch {
            shoppingAppRepo.addImage(uri).collect {
                Log.d("AddImage", it.toString())
            }
        }
    }

}