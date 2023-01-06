package com.go0ose.cryptocurrencyapp.presentation.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go0ose.cryptocurrencyapp.domain.CryptoInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val cryptoInteractor: CryptoInteractor
) : ViewModel() {

    private val _stateSplash = MutableStateFlow(false)
    val stateCoins: StateFlow<Boolean> get() = _stateSplash

    fun loadingData() {
        viewModelScope.launch {
            val listCoin =
            cryptoInteractor.getCryptoListFromApi("market_cap_desc", 1)
            cryptoInteractor.insertCryptoListToDataBase(listCoin)
            _stateSplash.value = true
        }
    }
}