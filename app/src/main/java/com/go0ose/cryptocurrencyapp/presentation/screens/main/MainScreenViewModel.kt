package com.go0ose.cryptocurrencyapp.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.SORT_BY_ALPHABETICALLY
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.SORT_BY_MARKET_CAP
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.SORT_BY_PRICE
import com.go0ose.cryptocurrencyapp.domain.CryptoInteractor
import com.go0ose.cryptocurrencyapp.presentation.model.Coin
import com.go0ose.cryptocurrencyapp.presentation.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val cryptoInteractor: CryptoInteractor
) : ViewModel() {

    private var page = 1
    var sortId = 2
    val items = mutableListOf<Coin>()

    private val _state = MutableStateFlow<UiState>(UiState.LoadingState)
    val state: StateFlow<UiState> = _state


    fun loadCoinsFromDataBase() {
        _state.value = UiState.SuccessState<List<Coin>>(emptyList())
        items.clear()
        viewModelScope.launch {
            val list = cryptoInteractor.getCryptoListFromDataBase()
            items.addAll(list)
            _state.value = UiState.SuccessState(list)
        }
    }

    fun loadNextPage() {
        _state.value = UiState.LoadingState
        page++
        loadCoinsFromApi()
    }


    fun refresh() {
        _state.value = UiState.LoadingState
        items.clear()
        page = 1
        loadCoinsFromApi()
    }

    private fun loadCoinsFromApi() {
        viewModelScope.launch {
            try {
                val list = cryptoInteractor.getCryptoListFromApi(chooseSortTypeById(sortId), page)
                items.addAll(list)
                _state.value = UiState.SuccessState(items)
            } catch (e: Throwable) {
                _state.value = UiState.ErrorState(e.message.toString())
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