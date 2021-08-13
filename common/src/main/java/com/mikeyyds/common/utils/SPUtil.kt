package com.mikeyyds.common.utils

import android.content.Context
import android.content.SharedPreferences
import com.mikeyyds.library.util.AppGlobals

object SPUtil{
    val CACHE_FILE = "cache_file"
    fun putString(key:String,value: String){
        val shared = getShared()
        if (shared!=null){
            shared.edit().putString(key,value).commit()
        }
    }

    fun getString(key:String):String?{
        val shared = getShared()
        if (shared!=null){
            return shared.getString(key,null)
        }
        return null
    }

    fun putBoolean(key:String,value: Boolean){
        val shared = getShared()
        if (shared!=null){
            shared.edit().putBoolean(key,value).commit()
        }
    }

    fun getBoolean(key:String):Boolean{
        val shared = getShared()
        if (shared!=null){
            return shared.getBoolean(key,false)
        }
        return false
    }

    fun putInt(key:String,value: Int){
        val shared = getShared()
        if (shared!=null){
            shared.edit().putInt(key,value).commit()
        }
    }

    fun getInt(key:String):Int?{
        val shared = getShared()
        if (shared!=null){
            return shared.getInt(key,0)
        }
        return null
    }

    fun getShared():SharedPreferences?{
        val application = AppGlobals.get()
        if (application!=null){
            return application.getSharedPreferences(CACHE_FILE,Context.MODE_PRIVATE)
        }
        return null
    }
}