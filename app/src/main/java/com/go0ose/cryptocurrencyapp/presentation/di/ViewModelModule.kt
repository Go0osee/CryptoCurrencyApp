package com.go0ose.cryptocurrencyapp.presentation.di

import com.go0ose.cryptocurrencyapp.presentation.screen.mainscreen.MainScreenViewModel
import com.go0ose.cryptocurrencyapp.presentation.screen.splashscreen.SplashScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SplashScreenViewModel(
            cryptoInteractor = get()
        )
    }
    viewModel {
        MainScreenViewModel(
            cryptoInteractor = get()
        )
    }
}