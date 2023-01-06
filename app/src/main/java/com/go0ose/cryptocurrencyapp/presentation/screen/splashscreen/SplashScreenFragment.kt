package com.go0ose.cryptocurrencyapp.presentation.screen.splashscreen

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.go0ose.cryptocurrencyapp.R
import com.go0ose.cryptocurrencyapp.databinding.FragmentSplashScreenBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private val binding: FragmentSplashScreenBinding by viewBinding()
    private val viewModel: SplashScreenViewModel by viewModel()
    private lateinit var animation: AnimatedVectorDrawableCompat

    override fun onStart() {
        super.onStart()

        initAnimation()
        initObserver()
        initLoadingData()
    }

    private fun initObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.stateCoins.collect { state ->
                if (state) {
                    findNavController().navigate(R.id.mainScreenFragment)
                    animation.stop()
                }
            }
        }
    }

    private fun initAnimation() {
        animation =
            AnimatedVectorDrawableCompat.create(requireContext(), R.drawable.anim_loading)!!
        animation.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                binding.animImage.post { animation.start() }
            }
        })
        binding.animImage.setImageDrawable(animation)
        animation.start()
    }

    private fun initLoadingData() {
        viewModel.loadingData()
    }
}