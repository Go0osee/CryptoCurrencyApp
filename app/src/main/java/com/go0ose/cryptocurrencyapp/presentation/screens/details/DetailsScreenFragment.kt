package com.go0ose.cryptocurrencyapp.presentation.screens.details

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.go0ose.cryptocurrencyapp.R
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.ALL
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.ONE_DAY
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.ONE_MONTH
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.ONE_WEEK
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.ONE_YEAR
import com.go0ose.cryptocurrencyapp.databinding.FragmentDetailsScreenBinding
import com.go0ose.cryptocurrencyapp.presentation.model.CoinDetails
import com.go0ose.cryptocurrencyapp.presentation.model.DetailsState
import com.go0ose.cryptocurrencyapp.utils.formatMarketCap
import com.go0ose.cryptocurrencyapp.utils.setImageByUrl
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsScreenFragment : Fragment(R.layout.fragment_details_screen) {

    private val binding: FragmentDetailsScreenBinding by viewBinding()
    private val viewModel: DetailsScreenViewModel by viewModel()
    private lateinit var animation: AnimatedVectorDrawableCompat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
        initAnimation()
        initToolbar(
            arguments?.getString("icon"),
            arguments?.getString("name")
        )
        initObservers()
        viewModel.id = arguments?.getString("id")!!
        viewModel.loadDetails(ONE_DAY)
    }

    private fun initAnimation() {
        animation =
            AnimatedVectorDrawableCompat.create(requireContext(), R.drawable.anim_loading)!!
        binding.animImage.setImageDrawable(animation)
    }

    private fun initToolbar(icon: String?, name: String?) {
        binding.icon.setImageByUrl(icon)
        binding.name.text = name
    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is DetailsState.SuccessState -> {
                        binding.animImage.visibility = View.GONE
                        animation.stop()
                        initView(state.coinDetails)
                    }
                    is DetailsState.LoadingState -> {
                        binding.animImage.visibility = View.VISIBLE
                        animation.start()
                    }
                    is DetailsState.ErrorState -> {}
                }
            }
        }
    }

    private fun initClickListeners() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            item1day.setOnClickListener {
                deselectAll()
                it.setBackgroundResource(R.drawable.background_item_chart)
                (it as TextView).setTextColor(Color.WHITE)
                viewModel.loadDetails(ONE_DAY)
            }
            item7days.setOnClickListener {
                deselectAll()
                it.setBackgroundResource(R.drawable.background_item_chart)
                (it as TextView).setTextColor(Color.WHITE)
                viewModel.loadDetails(ONE_WEEK)
            }
            item30days.setOnClickListener {
                deselectAll()
                it.setBackgroundResource(R.drawable.background_item_chart)
                (it as TextView).setTextColor(Color.WHITE)
                viewModel.loadDetails(ONE_MONTH)
            }
            item365days.setOnClickListener {
                deselectAll()
                it.setBackgroundResource(R.drawable.background_item_chart)
                (it as TextView).setTextColor(Color.WHITE)
                viewModel.loadDetails(ONE_YEAR)
            }
            itemAll.setOnClickListener {
                deselectAll()
                it.setBackgroundResource(R.drawable.background_item_chart)
                (it as TextView).setTextColor(Color.WHITE)
                viewModel.loadDetails("")
                viewModel.loadDetails(ALL)
            }
        }
    }

    private fun deselectAll() {
        with(binding) {
            item1day.setBackgroundColor(Color.WHITE)
            item7days.setBackgroundColor(Color.WHITE)
            item30days.setBackgroundColor(Color.WHITE)
            item365days.setBackgroundColor(Color.WHITE)
            itemAll.setBackgroundColor(Color.WHITE)

            item1day.setTextColor(requireContext().getColor(R.color.gray))
            item7days.setTextColor(requireContext().getColor(R.color.gray))
            item30days.setTextColor(requireContext().getColor(R.color.gray))
            item365days.setTextColor(requireContext().getColor(R.color.gray))
            itemAll.setTextColor(requireContext().getColor(R.color.gray))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView(coinDetails: CoinDetails) {
        with(binding) {
            price.text = "%.2f".format(arguments?.getDouble("price")) + " $"
            marketCap.text = "$ " + arguments?.getDouble("marketCap")?.let { formatMarketCap(it) }
            changePercentage.text = coinDetails.changePercentage
            maxPrice.text = coinDetails.maxPrice
            minPrice.text = coinDetails.minPrice

        }
    }
}