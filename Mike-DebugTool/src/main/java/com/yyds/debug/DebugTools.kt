package com.yyds.debug

import android.content.Intent
import android.os.Process.killProcess
import android.os.Process.myPid
import com.mikeyyds.common.utils.SPUtil
import com.mikeyyds.library.util.AppGlobals

class DebugTools {
    fun buildVersion():String{
        return "Build Version: "+BuildConfig.VERSION_CODE+"."+BuildConfig.VERSION_CODE
    }

    fun buildTime():String{
        return "Build Time: "+BuildConfig.BUILD_TIME
    }

    fun buildEnvironment():String{
        return "Build Env: "
    }

    @MikeDebug(name = "Degrade HTTP",desc = "Fiddle")
    fun degrade2Http(){
        SPUtil.putBoolean("degrade_http",true)
        val context = AppGlobals.get()?.applicationContext?:return
        val intent =
            context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)

        killProcess(myPid())

    }
}