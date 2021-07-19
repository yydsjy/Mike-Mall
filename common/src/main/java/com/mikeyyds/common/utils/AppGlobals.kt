package com.mikeyyds.common.utils

import android.app.Application

object AppGlobals {
    var application: Application? = null
    fun get(): Application? {
        if (application==null) {
            try {
                val application =
                    Class.forName("android.app.ActivityThread").getMethod("currentApplication")
                        .invoke(null) as Application
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return application
    }
}