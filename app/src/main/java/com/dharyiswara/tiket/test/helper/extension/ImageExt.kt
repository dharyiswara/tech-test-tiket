package com.dharyiswara.tiket.test.helper.extension

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dharyiswara.tiket.test.R

fun ImageView.loadFromUrl(
    imageUrl: String?,
    placeHolder: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_no_image)
) {
    val options = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .priority(Priority.IMMEDIATE)
    Glide.with(this.context)
        .load(imageUrl)
        .placeholder(placeHolder)
        .error(placeHolder)
        .apply(options)
        .into(this)
}