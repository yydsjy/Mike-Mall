package com.mikeyyds.mall.route

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.alibaba.android.arouter.launcher.ARouter
import java.lang.RuntimeException

@Interceptor(name ="biz_interceptor", priority = 9)
class BizInterceptor : IInterceptor {
    private var context: Context? = null
    override fun init(context: Context?) {
        this.context = context
    }

    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        val flag = postcard!!.extra
        if ((flag and RouteFlag.FLAG_LOGIN) != 0) {
            callback!!.onInterrupt(RuntimeException("need login"))
            loginIntercept()
        } else if ((flag and RouteFlag.FLAG_AUTHENTICATION) != 0) {
            callback!!.onInterrupt(RuntimeException("need authentication"))
            showToast("Please authenticate")
        } else if ((flag and RouteFlag.FLAG_VIP) != 0) {
            callback!!.onInterrupt(RuntimeException("need VIP"))
            showToast("Please apply VIP")
        } else {
            callback!!.onContinue(postcard)
        }


    }

    private fun loginIntercept() {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context,"Please Login",Toast.LENGTH_SHORT).show()
            ARouter.getInstance().build("/account/login").navigation()
        }
    }

    private fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post({Toast.makeText(context, message, Toast.LENGTH_SHORT).show()})
    }
}