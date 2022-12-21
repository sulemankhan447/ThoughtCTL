package com.thoughtctl.utils

import com.thoughtctl.Constants
import com.thoughtctl.model.Image
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {


    fun getDateTimeOfPost(image: ArrayList<Image>):String? {
        return if (image?.isNotEmpty() == true) {
            try{
                val timeStamp = image.first().datetime
                val dv: Long = (timeStamp?:0L) * 1000
                // its need to be in milisecond

                val df = Date(dv)
                return SimpleDateFormat(Constants.POST_DATE_FORMAT, Locale.getDefault()).format(df)
            }
            catch(e:Exception){
                e.printStackTrace()
                ""
            }

        } else "Today"
    }
}