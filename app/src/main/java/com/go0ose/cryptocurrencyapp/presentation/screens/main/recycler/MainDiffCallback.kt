package com.go0ose.cryptocurrencyapp.presentation.screens.main.recycler

import androidx.recyclerview.widget.DiffUtil
import com.go0ose.cryptocurrencyapp.presentation.model.Coin

class MainDiffCallback(private val oldItems: List<Coin>, private val newItems: List<Coin>) :
    DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }
}