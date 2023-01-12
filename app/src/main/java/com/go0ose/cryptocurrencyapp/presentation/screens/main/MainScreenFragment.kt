package com.go0ose.cryptocurrencyapp.presentation.screens.main

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.go0ose.cryptocurrencyapp.R
import com.go0ose.cryptocurrencyapp.databinding.FragmentMainScreenBinding
import com.go0ose.cryptocurrencyapp.presentation.model.Coin
import com.go0ose.cryptocurrencyapp.presentation.model.MainState
import com.go0ose.cryptocurrencyapp.presentation.screens.main.recycler.MainScreenAdapter
import com.go0ose.cryptocurrencyapp.presentation.screens.main.recycler.OnItemClickListener
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
    private var recyclerViewState: Parcelable? = null
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_screen, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
            "recyclerViewState",
            recyclerView.layoutManager?.onSaveInstanceState()
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            recyclerViewState = savedInstanceState.getParcelable("recyclerViewState")
            recyclerViewState?.let { recyclerView.layoutManager?.onRestoreInstanceState(it) }
        } else {
            viewModel.loadCoinsFromDataBase()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

                    if (viewModel.state.value != MainState.LoadingState &&
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
            viewModel.state.collectLatest { state ->
                when (state) {
                    is MainState.SuccessState -> {
                        binding.animImage.visibility = View.GONE
                        animation.stop()
                        if (!(adapter.items.any { state.list.map { it.id }.contains(it.id) })){
                            adapter.submitList(state.list)
                        }
                        binding.swipeRefresh.isRefreshing = false
                    }
                    is MainState.LoadingState -> {
                        binding.animImage.visibility = View.VISIBLE
                        animation.start()
                    }
                    is MainState.ErrorState -> {
                        binding.animImage.visibility = View.GONE
                        animation.stop()
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initAnimation() {
        animation =
            AnimatedVectorDrawableCompat.create(requireContext(), R.drawable.anim_loading)!!
        binding.animImage.setImageDrawable(animation)
    }

    private fun initSwipeRefreshListener() {
        binding.swipeRefresh.setOnRefreshListener {
            adapter.clearList()
            viewModel.refresh()
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

     fun createBundle(coin:Coin) = Bundle().apply {
         putString("icon", coin.image)
         putString("name", coin.name)
         putString("id", coin.id)
         putDouble("price", coin.currentPrice)
         putDouble("marketCap", coin.marketCap)
     }
}