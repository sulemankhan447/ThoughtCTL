package com.thoughtctl.api

sealed class UiState<T>() {
    class Loading<T> : UiState<T>()
    class Success<T>(val response: T?=null) : UiState<T>()
    class Error<T>(val error: Throwable) : UiState<T>()

}
