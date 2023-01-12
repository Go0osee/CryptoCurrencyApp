package com.go0ose.cryptocurrencyapp.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.SORT_BY_MARKET_CAP
import com.go0ose.cryptocurrencyapp.domain.CryptoInteractor
import com.go0ose.cryptocurrencyapp.presentation.model.SplashState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val cryptoInteractor: CryptoInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<SplashState>(SplashState.LoadingState)
    val state: StateFlow<SplashState> get() = _state


    fun loadingData() {
        viewModelScope.launch {
            try {
                cryptoInteractor.insertCryptoListToDataBase(
                    cryptoInteractor.getCryptoListFromApi(SORT_BY_MARKET_CAP, 1)
                )
                _state.value = SplashState.SuccessState
            } catch (e: Throwable) {
                _state.value = SplashState.ErrorState(e.message.toString())
            }
        }
    }
}