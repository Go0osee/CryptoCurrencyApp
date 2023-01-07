package com.go0ose.cryptocurrencyapp.presentation.screen.mainscreen.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go0ose.cryptocurrencyapp.presentation.model.Coin

class MainScreenAdapter : RecyclerView.Adapter<MainScreenViewHolder>() {

    var items: MutableList<Coin> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainScreenViewHolder {
        return MainScreenViewHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: MainScreenViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun submitList(data: List<Coin>) {
        items.addAll(data)
        notifyDataSetChanged()
    }

    fun refresh() {
        items.clear()
        notifyDataSetChanged()
    }
}