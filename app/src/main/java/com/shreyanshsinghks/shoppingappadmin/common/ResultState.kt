package com.shreyanshsinghks.shoppingappadmin.common

sealed class ResultState<out T> {
    data class Success<T>(val data: T) : ResultState<T>()
    object Loading : ResultState<Nothing>()
    data class Error(val message: String) : ResultState<Nothing>()

}