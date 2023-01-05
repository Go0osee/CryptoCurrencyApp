package com.go0ose.cryptocurrencyapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.go0ose.cryptocurrencyapp.R
import com.go0ose.cryptocurrencyapp.databinding.ActivityMainBinding
import com.go0ose.cryptocurrencyapp.presentation.splashscreen.SplashScreenFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openSplashScreen()
    }

    private fun openSplashScreen() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .setReorderingAllowed(false)
            .replace(R.id.nav_host_fragment, SplashScreenFragment())
            .commit()
    }
}