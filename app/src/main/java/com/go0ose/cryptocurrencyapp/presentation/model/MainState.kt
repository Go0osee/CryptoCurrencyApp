package com.go0ose.cryptocurrencyapp.presentation.model

sealed class MainState {

    object LoadingState : MainState()
    class SuccessState(val list: List<Coin>) : MainState()
    class ErrorState(val message: String) : MainState()
}