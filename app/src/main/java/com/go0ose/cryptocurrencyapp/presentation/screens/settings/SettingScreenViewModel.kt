package com.go0ose.cryptocurrencyapp.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go0ose.cryptocurrencyapp.data.storage.entity.UserEntity
import com.go0ose.cryptocurrencyapp.domain.CryptoInteractor
import com.go0ose.cryptocurrencyapp.presentation.model.SettingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingScreenViewModel(
    private val cryptoInteractor: CryptoInteractor
) : ViewModel() {

    var imageUri: String = ""
    var hasUser = false

    private val _state = MutableStateFlow<SettingState>(SettingState.SaveButtonInactive)
    val state: StateFlow<SettingState> = _state

    init {
        initLoadingUserFromDatabase()
    }

    private fun initLoadingUserFromDatabase() {
        cryptoInteractor.getUserFromDatabase().map { user ->
            if (user != null) {
                imageUri = user.avatar
                _state.value = SettingState.LoadedUserFromDatabase(user)
                hasUser = true
            }
        }.launchIn(viewModelScope)
    }

    fun saveToDataBase(firstName: String, lastName: String, dayOfBirth: String) {
        viewModelScope.launch {

            val user = UserEntity(
                id = 1,
                avatar = imageUri,
                firstName = firstName,
                lastName = lastName,
                dayOfBirth = dayOfBirth
            )
            if (hasUser) {
                cryptoInteractor.updateUser(user)
            } else {
                cryptoInteractor.insertUser(user)
            }
        }.invokeOnCompletion {
            _state.value = SettingState.SuccessSave
        }
    }

    fun setState(state: SettingState) {
        _state.value = state
    }

}