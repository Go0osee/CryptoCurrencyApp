package com.go0ose.cryptocurrencyapp.presentation.screens.details

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.go0ose.cryptocurrencyapp.R
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.ALL
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.ONE_DAY
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.ONE_MONTH
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.ONE_WEEK
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.ONE_YEAR
import com.go0ose.cryptocurrencyapp.databinding.FragmentDetailsScreenBinding
import com.go0ose.cryptocurrencyapp.presentation.model.CoinDetails
import com.go0ose.cryptocurrencyapp.presentation.model.UiState
import com.go0ose.cryptocurrencyapp.utils.*
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
            arguments?.getString(ICON),
            arguments?.getString(NAME)
        )
        initObservers()
        viewModel.id = arguments?.getString(ID)!!
        viewModel.loadDetails(ONE_DAY)
    }

    private fun initAnimation() {
        animation =
            AnimatedVectorDrawableCompat.create(requireContext(), R.drawable.anim_loading)!!
        animation.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                animation.start()
            }
        })
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
                    is UiState.SuccessState<*> -> {
                        binding.animImage.visibility = View.GONE
                        animation.stop()
                        initView(state.data as CoinDetails)
                    }
                    is UiState.LoadingState -> {
                        binding.animImage.visibility = View.VISIBLE
                        animation.start()
                    }
                    is UiState.ErrorState -> {
                        binding.animImage.visibility = View.GONE
                        animation.stop()
                        requireContext().showToast(requireContext().decipherError(state.message))
                    }
                }
            }
        }
    }

    private fun initClickListeners() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            item1day.setOnClickListener {
                deselectAll()
                it.setBackgroundResource(R.drawable.background_item_chart)
                item1day.setTextColor(Color.WHITE)
                viewModel.loadDetails(ONE_DAY)
            }
            item7days.setOnClickListener {
                deselectAll()
                it.setBackgroundResource(R.drawable.background_item_chart)
                item7days.setTextColor(Color.WHITE)
                viewModel.loadDetails(ONE_WEEK)
            }
            item30days.setOnClickListener {
                deselectAll()
                it.setBackgroundResource(R.drawable.background_item_chart)
                item30days.setTextColor(Color.WHITE)
                viewModel.loadDetails(ONE_MONTH)
            }
            item365days.setOnClickListener {
                deselectAll()
                it.setBackgroundResource(R.drawable.background_item_chart)
                item365days.setTextColor(Color.WHITE)
                viewModel.loadDetails(ONE_YEAR)
            }
            itemAll.setOnClickListener {
                deselectAll()
                it.setBackgroundResource(R.drawable.background_item_chart)
                itemAll.setTextColor(Color.WHITE)
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

            val cPrice = arguments?.getDouble(PRICE)!!
            val format = viewModel.chooseFormat(cPrice)

            price.text = format.format(cPrice) + " $"
            marketCap.text = "$ " + arguments?.getDouble(MARKET_CAP)?.let { formatMarketCap(it) }
            changePercentage.text = coinDetails.changePercentage
            maxPrice.text = format.format(coinDetails.maxPrice) + " $"
            minPrice.text = format.format(coinDetails.minPrice) + " $"
            initChart(coinDetails.listEntry)

        }
    }

    private fun initChart(listEntry: List<Entry>) {
        with(binding.chart) {

            xAxis.setDrawLabels(false)
            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(false)
            axisRight.setDrawLabels(false)
            axisRight.setDrawGridLines(false)
            axisRight.setDrawAxisLine(false)
            axisLeft.setDrawLabels(false)
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawAxisLine(false)

            description.isEnabled = false
            legend.isEnabled = false

            val set = LineDataSet(listEntry, CHART).apply {
                color = getColor(requireContext(), R.color.second_color)
                lineWidth = 2f
                setDrawCircles(false)
            }

            data = LineData(set)
            invalidate()
        }
    }
}