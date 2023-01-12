package com.go0ose.cryptocurrencyapp.presentation.screens.splash

import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.go0ose.cryptocurrencyapp.R
import com.go0ose.cryptocurrencyapp.databinding.FragmentSplashScreenBinding
import com.go0ose.cryptocurrencyapp.presentation.model.SplashState
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
            viewModel.state.collect { state ->

                when(state){
                    is SplashState.SuccessState ->{
                        findNavController().navigate(R.id.mainScreenFragment)
                        animation.stop()
                    }
                    is SplashState.LoadingState ->{
                        animation.start()
                    }
                    is SplashState.ErrorState ->{
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
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
    }

    private fun initLoadingData() {
        viewModel.loadingData()
    }
}