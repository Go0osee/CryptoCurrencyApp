package com.go0ose.cryptocurrencyapp.presentation.screen.mainscreen

import android.app.AlertDialog
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
import com.go0ose.cryptocurrencyapp.presentation.screen.mainscreen.recycler.MainScreenAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private val binding: FragmentMainScreenBinding by viewBinding()
    private val viewModel: MainScreenViewModel by viewModel()
    private var adapter = MainScreenAdapter()
    private lateinit var animation: AnimatedVectorDrawableCompat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        loadCoinsFromDataBase()
        viewModel.refresh()
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
                viewModel.setLoadingState(false)
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
            adapter.refresh()
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

        AlertDialog.Builder(this.requireContext())
            .setTitle(resources.getString(R.string.sort))
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                viewModel.sortId = dSortId
                adapter.refresh()
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