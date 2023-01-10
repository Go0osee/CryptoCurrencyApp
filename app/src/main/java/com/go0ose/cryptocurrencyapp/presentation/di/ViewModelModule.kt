package com.go0ose.cryptocurrencyapp.presentation.di

import com.go0ose.cryptocurrencyapp.presentation.screens.details.DetailsScreenViewModel
import com.go0ose.cryptocurrencyapp.presentation.screens.main.MainScreenViewModel
import com.go0ose.cryptocurrencyapp.presentation.screens.splash.SplashScreenViewModel
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
    viewModel {
        DetailsScreenViewModel(
            cryptoInteractor = get()
        )
    }
}