package com.go0ose.cryptocurrencyapp.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.SORT_BY_ALPHABETICALLY
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.SORT_BY_MARKET_CAP
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.SORT_BY_PRICE
import com.go0ose.cryptocurrencyapp.domain.CryptoInteractor
import com.go0ose.cryptocurrencyapp.presentation.model.MainState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val cryptoInteractor: CryptoInteractor
) : ViewModel() {

    private var page = 1
    var sortId = 2

    private val _state = MutableStateFlow<MainState>(MainState.LoadingState)
    val state: StateFlow<MainState> = _state


    fun loadCoinsFromDataBase() {
        _state.value = MainState.SuccessState(emptyList())
        viewModelScope.launch {
            _state.value = MainState.SuccessState(cryptoInteractor.getCryptoListFromDataBase())
        }
    }

    fun loadNextPage() {
        _state.value = MainState.LoadingState
        page++
        loadCoinsFromApi()
    }


    fun refresh() {
        _state.value = MainState.LoadingState
        page = 1
        loadCoinsFromApi()
    }

    private fun loadCoinsFromApi() {
        viewModelScope.launch {
            try {
                _state.value = MainState.SuccessState(
                    cryptoInteractor.getCryptoListFromApi(chooseSortTypeById(sortId), page)
                )
            } catch (e: Throwable) {
                _state.value = MainState.ErrorState(e.message.toString())
            }
        }
    }

    private fun chooseSortTypeById(id: Int): String {
        return when (id) {
            0 -> SORT_BY_PRICE
            1 -> SORT_BY_ALPHABETICALLY
            else -> SORT_BY_MARKET_CAP
        }
    }
}