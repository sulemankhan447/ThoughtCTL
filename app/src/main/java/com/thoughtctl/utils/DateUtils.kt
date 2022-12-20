package com.thoughtctl.utils

import com.thoughtctl.model.Image

object DateUtils {


    fun getDateTimeOfPost(image: ArrayList<Image>):String? {
        return if (image?.isNotEmpty() == true) image.first().datetime?.toString() else "Today"
    }
}