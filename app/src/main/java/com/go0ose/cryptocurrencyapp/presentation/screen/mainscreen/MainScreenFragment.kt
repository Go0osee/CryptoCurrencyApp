package com.go0ose.cryptocurrencyapp.presentation.screen.mainscreen

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.go0ose.cryptocurrencyapp.R
import com.go0ose.cryptocurrencyapp.databinding.FragmentMainScreenBinding
import com.go0ose.cryptocurrencyapp.presentation.screen.mainscreen.recycler.MainScreenAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private val binding: FragmentMainScreenBinding by viewBinding()
    private val viewModel: MainScreenViewModel by viewModel()
    private var adapter = MainScreenAdapter()

    override fun onStart() {
        super.onStart()


    }
}