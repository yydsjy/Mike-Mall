package com.mikeyyds.mall.route

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.mikeyyds.library.util.AppGlobals
import java.lang.Exception

object MikeRoute {
    fun startActivity4Browser(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            AppGlobals.get()?.startActivity(intent)
        } catch (e:Exception){
            e.printStackTrace()
        }
    }

    enum class Destination(val path: String,val desc:String) {
        ACCOUNT_REGISTRATION("/account/registration","register"),
        ACCOUNT_LOGIN("/account/login","login"),
        DEGRADE_GLOBAL("/degrade/global/activity","degrade"),
        GOODS_LIST("/goods/list","goods list"),
        DETAIL_MAIN("/detail/main","goods detail"),
        NOTICE_LIST("/notice/list","notice list")
    }

    fun startActivity(
        context: Context?,
        bundle: Bundle?=null,
        destination: Destination,
        requestCode: Int = -1
    ) {
        val postcard = ARouter.getInstance().build(destination.path).with(bundle)
        if (requestCode==-1||context !is Activity){
            postcard.navigation(context)
        } else{
            postcard.navigation(context,requestCode)
        }
    }

    fun inject(target: Any){
        ARouter.getInstance().inject(target)
    }
}