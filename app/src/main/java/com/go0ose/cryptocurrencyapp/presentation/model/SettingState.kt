package com.go0ose.cryptocurrencyapp.presentation.model

import com.go0ose.cryptocurrencyapp.data.storage.entity.UserEntity

sealed class SettingState {
    object SaveButtonActive : SettingState()
    object SaveButtonInactive : SettingState()
    object SuccessSave : SettingState()
    class LoadedUserFromDatabase(val user: UserEntity) : SettingState()
}
