package com.test.myplaylist.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.test.myplaylist.R

fun ImageView.loadImage(url: String, drawableRes: Int = R.drawable.icon_placeholder) {
    Glide
        .with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .error(drawableRes)
        .into(this)
}