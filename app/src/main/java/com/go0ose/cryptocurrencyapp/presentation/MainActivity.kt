package com.go0ose.cryptocurrencyapp.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.go0ose.cryptocurrencyapp.R
import com.go0ose.cryptocurrencyapp.databinding.ActivityMainBinding
import com.go0ose.cryptocurrencyapp.utils.showNoConnectionDialog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            showNoConnectionDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        initNavController()
        registerReceiver(broadcastReceiver, IntentFilter("show_dialog"))
        startService(Intent(this, CheckConnectionService()::class.java))
    }


    override fun onStop() {
        super.onStop()
        stopService(Intent(this, CheckConnectionService()::class.java))
        unregisterReceiver(broadcastReceiver)
    }

    private fun initNavController() {
        val navController = findNavController(this, R.id.navHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashScreenFragment -> binding.bottomNavigation.visibility = View.GONE
                R.id.mainScreenFragment -> binding.bottomNavigation.visibility = View.VISIBLE
                R.id.settingScreenFragment -> binding.bottomNavigation.visibility = View.VISIBLE
                R.id.detailsScreenFragment -> binding.bottomNavigation.visibility = View.GONE
            }
        }
    }
}