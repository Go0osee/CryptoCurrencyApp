package com.go0ose.cryptocurrencyapp.presentation.splashscreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SplashScreenViewModel(

) : ViewModel() {
    private val _stateSplash = MutableStateFlow(false)
    val stateCoins: StateFlow<Boolean> get() = _stateSplash
}