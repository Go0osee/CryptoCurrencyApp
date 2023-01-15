package com.go0ose.cryptocurrencyapp.presentation.model

sealed class UiState {

    object LoadingState : UiState()
    class SuccessState<T : Any>(val data: T) : UiState()
    class ErrorState(val message: String) : UiState()
}