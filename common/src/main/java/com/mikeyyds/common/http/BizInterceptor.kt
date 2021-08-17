package com.mikeyyds.common.http

import com.mikeyyds.common.utils.SPUtil
import com.mikeyyds.library.log.MikeLog
import com.mikeyyds.library.restful.MikeInterceptor

class BizInterceptor: MikeInterceptor{
    override fun intercept(chain: MikeInterceptor.Chain): Boolean {
        if (chain.isRequestPeriod){
            val request = chain.request()
            val boardingPass = SPUtil.getString("boarding-pass") ?: ""
            request.addHeader("auth-token","MTU5Mjg1MDg3NDcwNw11.26==")
            request.addHeader("boarding-pass",boardingPass)
        } else if (chain.response()!=null){
            MikeLog.dt("BizInterceptor",chain.request().endPointUrl())
            MikeLog.dt("BizInterceptor",chain.response()!!.rawData)
        }
        return false;
    }
}