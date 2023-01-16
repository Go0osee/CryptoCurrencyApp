package com.go0ose.cryptocurrencyapp.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.go0ose.cryptocurrencyapp.R

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
fun FragmentActivity.showNoConnectionDialog() {
    val builder = AlertDialog.Builder(this)
    val dialogLayout =
        layoutInflater.inflate(R.layout.dialog_alert_no_internet_connection, null)
    val positiveButton = dialogLayout.findViewById<TextView>(R.id.tryAgain)
    builder.setCancelable(false)
    builder.setView(dialogLayout)

    val alertDialog = builder.create()
    alertDialog.window?.setBackgroundDrawable(
        ColorDrawable(Color.TRANSPARENT)
    )
    positiveButton.setOnClickListener {
        alertDialog.cancel()
        if(!isOnline()){
            showNoConnectionDialog ()
        }
    }
    alertDialog.show()
}

fun Context.isOnline(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        return true
    }
    return false
}

fun Context.showToast(message: String) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}