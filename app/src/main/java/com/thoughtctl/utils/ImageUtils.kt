package com.thoughtctl.utils

import android.content.Context
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide

object ImageUtils {


    fun loadImage(
        context: Context,
        url: String?,
        imageView: ImageView?,
        loadingPlaceholder: Int,
        errorPlaceholder: Int
    ) {
        try {
            if (imageView == null) {
                return
            }
            if (!TextUtils.isEmpty(url)) {
                Glide.with(context)
                    .load(url)
                    .placeholder(loadingPlaceholder)
                    .error(errorPlaceholder)
                    .into(imageView)
            } else {
                Glide.with(context)
                    .load(errorPlaceholder)
                    .into(imageView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}