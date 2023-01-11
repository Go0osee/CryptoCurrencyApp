package com.go0ose.cryptocurrencyapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.setImageByUrl(Url: String?) {
    Glide.with(context)
        .load(Url)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}