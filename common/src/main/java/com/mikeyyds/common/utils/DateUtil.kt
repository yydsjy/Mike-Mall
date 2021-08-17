package com.mikeyyds.common.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    private const val MD_FORMATE = "MM-dd"
    private const val DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss"
    fun getMDDate(date:Date):String{
        val sdf = SimpleDateFormat(MD_FORMATE)
        return sdf.format(date)
    }

    fun getMDDate(dateString:String):String{
        val sdf = SimpleDateFormat(MD_FORMATE)
        return getMDDate(sdf.parse(dateString))
    }


}