package com.go0ose.cryptocurrencyapp.presentation.model

sealed class DetailsState {

    object LoadingState : DetailsState()
    class SuccessState(val coinDetails: CoinDetails) : DetailsState()
    class ErrorState(val message: String) : DetailsState()

}