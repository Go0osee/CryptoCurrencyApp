package com.go0ose.cryptocurrencyapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.setImageByUrl(url: String?) {
    Glide.with(context)
        .load(url)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}

fun ImageView.setImageByUri(uri: String?) {
    Glide.with(context)
        .load(uri)
        .into(this)
}