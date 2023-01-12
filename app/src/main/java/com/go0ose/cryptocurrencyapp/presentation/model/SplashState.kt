package com.go0ose.cryptocurrencyapp.presentation.model

sealed class SplashState {

    object LoadingState : SplashState()
    object SuccessState : SplashState()
    class ErrorState(val message: String) : SplashState()
}