package com.test.myplaylist.extension

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@BindingAdapter(value = ["imageUrlProfileProgress", "placeholder"], requireAll = false)
fun ImageView.loadImageProfileProgress(imageUrl: String? = null, placeHolder: Drawable? = null) {
    imageUrl?.let { it ->
        Glide
            .with(context)
            .load(it)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(this)
    } ?: run {
    }
}