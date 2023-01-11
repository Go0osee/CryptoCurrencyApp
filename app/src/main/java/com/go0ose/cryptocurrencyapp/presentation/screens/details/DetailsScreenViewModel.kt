package com.go0ose.cryptocurrencyapp.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go0ose.cryptocurrencyapp.domain.CryptoInteractor
import com.go0ose.cryptocurrencyapp.presentation.model.DetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsScreenViewModel(
    private val cryptoInteractor: CryptoInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<DetailsState>(DetailsState.LoadingState)
    val state: StateFlow<DetailsState> = _state

    var id = ""
    private var amount = ""

    fun loadDetails(days: String) {
        if (days != amount) {
            _state.value = DetailsState.LoadingState
            amount = days
            viewModelScope.launch {
                try {
                    _state.value =
                        DetailsState.SuccessState(
                            cryptoInteractor.getCoinDetailsFromApi(
                                id,
                                amount
                            )
                        )
                } catch (e: Throwable) {
                    _state.value = DetailsState.ErrorState(e.message.toString())
                }
            }
        }
    }
}