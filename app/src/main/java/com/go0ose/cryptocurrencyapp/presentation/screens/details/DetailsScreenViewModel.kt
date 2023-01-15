package com.go0ose.cryptocurrencyapp.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go0ose.cryptocurrencyapp.domain.CryptoInteractor
import com.go0ose.cryptocurrencyapp.presentation.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsScreenViewModel(
    private val cryptoInteractor: CryptoInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.LoadingState)
    val state: StateFlow<UiState> = _state

    var id = ""
    private var amount = ""

    fun loadDetails(days: String) {
        if (days != amount) {
            _state.value = UiState.LoadingState
            amount = days
            viewModelScope.launch {
                try {
                    _state.value =
                        UiState.SuccessState(
                            cryptoInteractor.getCoinDetailsFromApi(id, amount)
                        )
                } catch (e: Throwable) {
                    _state.value = UiState.ErrorState(e.message.toString())
                }
            }
        }
    }

    fun chooseFormat(cPrice: Double): String {
        return when {
            cPrice < 0.0000001 -> "%.10f"
            cPrice < 0.000001 -> "%.9f"
            cPrice < 0.00001 -> "%.8f"
            cPrice < 0.0001 -> "%.7f"
            cPrice < 0.001 -> "%.6f"
            cPrice < 0.01 -> "%.5f"
            cPrice < 0.1 -> "%.4f"
            cPrice < 10 -> "%.3f"
            else -> "%.2f"
        }
    }
}