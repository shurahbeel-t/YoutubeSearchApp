package com.observer.youtubesearchapp.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.observer.youtubesearchapp.R


fun ImageView.loadImage(uri: String?) {
    val requestOptions = RequestOptions()
        .placeholder(R.drawable.ic_launcher_foreground)
        .error(R.drawable.ic_launcher_foreground)
    Glide.with(this)
        .setDefaultRequestOptions(requestOptions)
        .load(uri)
        .into(this)
}