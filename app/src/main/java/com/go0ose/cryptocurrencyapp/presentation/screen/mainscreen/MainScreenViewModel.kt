package com.go0ose.cryptocurrencyapp.presentation.screen.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.SORT_BY_ALPHABETICALLY
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.SORT_BY_MARKET_CAP
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.SORT_BY_PRICE
import com.go0ose.cryptocurrencyapp.domain.CryptoInteractor
import com.go0ose.cryptocurrencyapp.presentation.model.Coin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val cryptoInteractor: CryptoInteractor
) : ViewModel() {

    var page = 1
    var sortId = 2

    private val _coinsList = MutableStateFlow<List<Coin>>(emptyList())
    val coinsList: StateFlow<List<Coin>> get() = _coinsList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading


    fun loadCoinsFromDataBase() {
        viewModelScope.launch {
            _coinsList.value = cryptoInteractor.getCryptoListFromDataBase()
        }
    }

    fun loadNextPage() {
        page++
        setLoadingState(true)
        viewModelScope.launch {
            _coinsList.value =
                cryptoInteractor.getCryptoListFromApi(chooseSortTypeById(sortId), page)
        }
    }

    fun setLoadingState(boolean: Boolean) {
        _isLoading.value = boolean
    }

    fun refresh() {
        page = 1
        _coinsList.value = emptyList()
        viewModelScope.launch {
            _coinsList.value =
                cryptoInteractor.getCryptoListFromApi(chooseSortTypeById(sortId), page)
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