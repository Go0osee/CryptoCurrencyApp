package com.go0ose.cryptocurrencyapp.presentation.di

import com.go0ose.cryptocurrencyapp.presentation.splashscreen.SplashScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SplashScreenViewModel(
            cryptoInteractor = get()
        )
    }
}