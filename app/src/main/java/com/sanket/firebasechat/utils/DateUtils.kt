package com.sanket.firebasechat.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object {
        fun getDate(timestamp: Long, format: String): String {
            val date: Date = Date(timestamp)
            val dateFormatter = SimpleDateFormat(format, Locale.ENGLISH)
            return dateFormatter.format(date)
        }
    }

}