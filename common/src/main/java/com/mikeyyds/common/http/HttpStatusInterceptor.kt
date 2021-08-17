package com.mikeyyds.common.http

import com.alibaba.android.arouter.launcher.ARouter
import com.mikeyyds.library.restful.MikeInterceptor
import com.mikeyyds.library.restful.MikeResponse

class HttpStatusInterceptor : MikeInterceptor {

    override fun intercept(chain: MikeInterceptor.Chain): Boolean {
        val response = chain.response();
        if (response != null && !chain.isRequestPeriod) {
            val code = response.code
            when (code) {
                MikeResponse.RC_NEED_LOGIN -> {
                    ARouter.getInstance().build("/account/login").navigation()
                }
                MikeResponse.RC_AUTH_TOKEN_EXPIRED, MikeResponse.RC_AUTH_TOKEN_INVALID, MikeResponse.RC_USER_FORBID -> {
                    var helpUrl: String? = null
                    if (response.errorData != null) {
                        helpUrl = response.errorData!!.get("helpUrl")
                    }
                    ARouter.getInstance().build("/degrade/global/activity")
                        .withString("degrade_title", "illegal visit")
                        .withString("degrade_desc", response.msg)
                        .withString("degrade_action", helpUrl).navigation()
                }
            }
        }
        return false
    }
}