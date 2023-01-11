package com.go0ose.cryptocurrencyapp.presentation.screens.main.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go0ose.cryptocurrencyapp.presentation.model.Coin

class MainScreenAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MainScreenViewHolder>() {

    var items: MutableList<Coin> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainScreenViewHolder {
        return MainScreenViewHolder.newInstance(parent, onItemClickListener)
    }

    override fun onBindViewHolder(holder: MainScreenViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun submitList(data: List<Coin>) {
        items.addAll(data)
        notifyDataSetChanged()
    }

    fun clearList() {
        items.clear()
        notifyDataSetChanged()
    }
}