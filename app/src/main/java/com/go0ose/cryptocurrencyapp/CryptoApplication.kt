package com.go0ose.cryptocurrencyapp

import android.app.Application
import com.go0ose.cryptocurrencyapp.data.di.dataModule
import com.go0ose.cryptocurrencyapp.data.di.retrofitModule
import com.go0ose.cryptocurrencyapp.data.di.roomModule
import com.go0ose.cryptocurrencyapp.domain.di.domainModule
import com.go0ose.cryptocurrencyapp.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CryptoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CryptoApplication)
            modules(
                listOf(
                    dataModule,
                    retrofitModule,
                    roomModule,
                    domainModule,
                    viewModelModule
                )
            )
        }
    }
}
