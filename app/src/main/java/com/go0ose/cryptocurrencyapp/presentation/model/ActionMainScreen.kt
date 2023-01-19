package com.go0ose.cryptocurrencyapp.presentation.model

sealed class ActionMainScreen {
    object LoadNextPage : ActionMainScreen()
    object LoadCoinsFromDataBase : ActionMainScreen()
    object Refresh : ActionMainScreen()
}
