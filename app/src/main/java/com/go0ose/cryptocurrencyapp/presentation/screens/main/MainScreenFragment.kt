package com.go0ose.cryptocurrencyapp.presentation.screens.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.go0ose.cryptocurrencyapp.R
import com.go0ose.cryptocurrencyapp.databinding.FragmentMainScreenBinding
import com.go0ose.cryptocurrencyapp.presentation.model.ActionMainScreen
import com.go0ose.cryptocurrencyapp.presentation.model.Coin
import com.go0ose.cryptocurrencyapp.presentation.model.UiState
import com.go0ose.cryptocurrencyapp.presentation.screens.main.recycler.MainScreenAdapter
import com.go0ose.cryptocurrencyapp.presentation.screens.main.recycler.OnItemClickListener
import com.go0ose.cryptocurrencyapp.utils.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private val binding: FragmentMainScreenBinding by viewBinding()
    private val viewModel: MainScreenViewModel by viewModel()
    private lateinit var animation: AnimatedVectorDrawableCompat

    private val onItemListener by lazy {
        object : OnItemClickListener {
            override fun onItemClick(coin: Coin) {

                findNavController().navigate(
                    R.id.action_mainScreenFragment_to_detailsScreenFragment,
                    createBundle(coin)
                )
            }
        }
    }

    private var adapter = MainScreenAdapter(onItemListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        if (viewModel.items.isEmpty()) {
            viewModel.doWork(ActionMainScreen.LoadCoinsFromDataBase)
        }
        initAnimation()
        initObservers()
        initScrolledListener()
        initSwipeRefreshListener()
        initSortClickListener()
    }

    private fun initScrolledListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (layoutManager != null) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (viewModel.state.value != UiState.LoadingState &&
                        (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    ) {
                        viewModel.doWork(ActionMainScreen.LoadNextPage)
                    }
                }
            }
        })
    }

    @Suppress("UNCHECKED_CAST")
    private fun initObservers() {

        lifecycleScope.launchWhenStarted {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is UiState.SuccessState<*> -> {
                        binding.animImage.visibility = View.GONE
                        animation.stop()
                        adapter.updateItems((state.data as List<Coin>))
                        binding.swipeRefresh.isRefreshing = false
                    }
                    is UiState.LoadingState -> {
                        binding.animImage.visibility = View.VISIBLE
                        animation.start()
                    }
                    is UiState.ErrorState -> {
                        binding.animImage.visibility = View.GONE
                        animation.stop()
                        binding.swipeRefresh.isRefreshing = false
                        requireContext().showToast(requireContext().decipherError(state.message))
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
                animation.start()
            }
        })
        binding.animImage.setImageDrawable(animation)
    }

    private fun initSwipeRefreshListener() {
        binding.swipeRefresh.setOnRefreshListener {
            adapter.clearList()
            viewModel.doWork(ActionMainScreen.Refresh)
        }
    }

    private fun initSortClickListener() {
        binding.toolbar.menu.findItem(R.id.sort).setOnMenuItemClickListener {
            showSortAlertDialog()
            true
        }
    }

    private fun showSortAlertDialog() {

        var dSortId = 0

        MaterialAlertDialogBuilder(this.requireContext())
            .setTitle(resources.getString(R.string.sort))
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                viewModel.sortId = dSortId
                adapter.clearList()
                viewModel.doWork(ActionMainScreen.Refresh)
            }
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .setSingleChoiceItems(
                arrayOf(
                    getString(R.string.by_price),
                    getString(R.string.alphabetically)
                ),
                viewModel.sortId
            ) { _, sortId ->
                dSortId = sortId
            }
            .show()
    }

    fun createBundle(coin: Coin) = Bundle().apply {
        putString(ICON, coin.image)
        putString(NAME, coin.name)
        putString(ID, coin.id)
        putDouble(PRICE, coin.currentPrice)
        putDouble(MARKET_CAP, coin.marketCap)
    }
}