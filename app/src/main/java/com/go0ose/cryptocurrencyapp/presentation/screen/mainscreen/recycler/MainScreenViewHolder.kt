package com.go0ose.cryptocurrencyapp.presentation.screen.mainscreen.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go0ose.cryptocurrencyapp.databinding.ItemCoinBinding
import com.go0ose.cryptocurrencyapp.presentation.model.Coin
import com.go0ose.cryptocurrencyapp.utils.setImageByUrl

class MainScreenViewHolder(
    private val binding: ItemCoinBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun newInstance(
            parent: ViewGroup,
        ) =
            MainScreenViewHolder(
                ItemCoinBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }

    fun bind(item: Coin) {
        with(binding) {
            image.setImageByUrl(item.image)
            symbol.text = item.symbol
            name.text = item.name
            price.text = "${item.currentPrice} $"
            marketCap.text = "${item.marketCap.toLong()} $"
        }
    }
}