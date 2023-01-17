package com.go0ose.cryptocurrencyapp.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go0ose.cryptocurrencyapp.data.storage.entity.UserEntity
import com.go0ose.cryptocurrencyapp.domain.CryptoInteractor
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
    // TODO!@# Rewrite to sealed class?
    private val _stateSaveButton = MutableStateFlow<Boolean>(false)
    val stateSaveButton: StateFlow<Boolean> = _stateSaveButton

    // TODO!@# Remove explicit arguments of type in project
    private val _user = MutableStateFlow<UserEntity>(UserEntity(1, "", "", "", ""))
    val user: StateFlow<UserEntity> = _user

    private val _stateSave = MutableStateFlow<Boolean>(false)
    val stateSave: StateFlow<Boolean> = _stateSave

    init {
        runFlow()
    }

    // TODO!@# Bad method naming
    private fun runFlow() {
        // TODO!@# Same here
        cryptoInteractor.getFlowUser().map { user ->
            if(user != null) {
                imageUri = user.avatar
                _user.value = user
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
            _stateSave.value = true
            _stateSave.value = false
        }
    }

    fun setState(state: Boolean) {
        _stateSaveButton.value = state
    }

}