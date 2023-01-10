package com.go0ose.cryptocurrencyapp.presentation.screens.main.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go0ose.cryptocurrencyapp.databinding.ItemCoinBinding
import com.go0ose.cryptocurrencyapp.presentation.model.Coin
import com.go0ose.cryptocurrencyapp.utils.setImageByUrl

class MainScreenViewHolder(
    private val binding: ItemCoinBinding,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun newInstance(
            parent: ViewGroup,
            onItemClickListener: OnItemClickListener
        ) =
            MainScreenViewHolder(
                ItemCoinBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onItemClickListener
            )
    }

    fun bind(item: Coin) {
        with(binding) {
            image.setImageByUrl(item.image)
            symbol.text = item.symbol
            name.text = item.name
            price.text = "${item.currentPrice} $"
            marketCap.text = "${item.marketCap.toLong()} $"

            root.setOnClickListener {
                onItemClickListener.onItemClick(item)
            }
        }
    }
}