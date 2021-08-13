package com.mikeyyds.mall.route

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.alibaba.android.arouter.launcher.ARouter
import com.mikeyyds.library.util.MainHandler
import com.mikeyyds.mall.biz.account.AccountManager
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
            loginIntercept(postcard,callback)
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

    private fun loginIntercept(postcard: Postcard?,callback: InterceptorCallback?) {

        MainHandler.post(runnable = Runnable{

            if (AccountManager.isLogin()){
                callback?.onContinue(postcard)
            } else{
                showToast("Please Login")
                AccountManager.login(context, Observer {success ->
                    callback?.onContinue(postcard)
                })
            }
        })


    }

    private fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post({Toast.makeText(context, message, Toast.LENGTH_SHORT).show()})
    }
}