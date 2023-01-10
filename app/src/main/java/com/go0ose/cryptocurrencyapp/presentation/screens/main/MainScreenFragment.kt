package com.go0ose.cryptocurrencyapp.presentation.screens.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.go0ose.cryptocurrencyapp.R
import com.go0ose.cryptocurrencyapp.databinding.FragmentMainScreenBinding
import com.go0ose.cryptocurrencyapp.presentation.model.Coin
import com.go0ose.cryptocurrencyapp.presentation.screens.main.recycler.MainScreenAdapter
import com.go0ose.cryptocurrencyapp.presentation.screens.main.recycler.OnItemClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private val binding: FragmentMainScreenBinding by viewBinding()
    private val viewModel: MainScreenViewModel by viewModel()
    private lateinit var animation: AnimatedVectorDrawableCompat

    private val onItemListener by lazy {
        object : OnItemClickListener {
            override fun onItemClick(coin: Coin) {
                // переход
            }
        }
    }

    private var adapter = MainScreenAdapter(onItemListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        initAnimation()
        initObservers()
        initScrolledListener()
        initSwipeRefreshListener()
        initSortClickListener()
    }

    override fun onResume() {
        super.onResume()

        viewModel.sortId = 2
        adapter.clear()
        loadCoinsFromDataBase()
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

                    if (
                        !viewModel.isLoading.value &&
                        (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    ) {
                        viewModel.loadNextPage()
                    }
                }
            }
        })
    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.coinsList.collect { listCoins ->
                adapter.submitList(listCoins)
                if(listCoins.isNotEmpty()){
                    viewModel.setLoadingState(false)
                }
                binding.swipeRefresh.isRefreshing = false
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect { loadingState ->
                when (loadingState) {
                    true -> {
                        binding.animImage.visibility = View.VISIBLE
                        animation.start()
                    }
                    false -> {
                        binding.animImage.visibility = View.GONE
                        animation.stop()
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
//              binding.animImage.post { animation.start() }
            }
        })
        binding.animImage.setImageDrawable(animation)
    }

    private fun loadCoinsFromDataBase() {
        viewModel.loadCoinsFromDataBase()
    }

    private fun initSwipeRefreshListener() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.setLoadingState(true)
            adapter.clear()
            viewModel.refresh()
        }
    }

    private fun initSortClickListener() {
        binding.toolbar.menu.findItem(R.id.sort).setOnMenuItemClickListener {
            showAlertDialog()
            true
        }
    }

    private fun showAlertDialog() {

        var dSortId = 0

        MaterialAlertDialogBuilder(this.requireContext())
            .setTitle(resources.getString(R.string.sort))
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                viewModel.sortId = dSortId
                viewModel.setLoadingState(true)
                adapter.clear()
                viewModel.refresh()
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
}