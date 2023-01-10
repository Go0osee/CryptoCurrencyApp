package com.go0ose.cryptocurrencyapp.presentation.screens.main.recycler

import com.go0ose.cryptocurrencyapp.presentation.model.Coin

interface OnItemClickListener {

    fun onItemClick(coin: Coin)
}