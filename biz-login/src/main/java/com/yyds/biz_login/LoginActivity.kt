package com.yyds.biz_login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.mikeyyds.common.ui.component.MikeBaseActivity
import com.mikeyyds.common.ui.view.InputItemLayout
import com.mikeyyds.library.restful.MikeCallback
import com.mikeyyds.library.restful.MikeResponse
import com.mikeyyds.library.util.MikeStatusBar
import com.mikeyyds.common.http.ApiFactory

@Route(path = "/account/login")
class LoginActivity : MikeBaseActivity() {
    private val REQUEST_CODE_REGISTRATION: Int = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        findViewById<TextView>(R.id.action_back).setOnClickListener {
            onBackPressed()
        }

        findViewById<TextView>(R.id.action_register).setOnClickListener {
            goRegister()
        }

        findViewById<Button>(R.id.action_login).setOnClickListener {
            goLogin()
        }

        MikeStatusBar.setStatusBar(this,true, Color.WHITE,false)
    }

    private fun goLogin() {
        val name = findViewById<InputItemLayout>(R.id.input_item_user_name).getEditText().text
        val password = findViewById<InputItemLayout>(R.id.input_item_password).getEditText().text

        if (TextUtils.isEmpty(name) or TextUtils.isEmpty(password)) {
            return
        }

        ApiFactory.create(com.yyds.biz_login.api.AccountApi::class.java).login(name.toString(), password.toString())
            .enqueue(object : MikeCallback<String> {
                override fun onSuccess(response: MikeResponse<String>) {
                    if (response.code == MikeResponse.SUCCESS){
                        showToast(getString(R.string.login_successfully))
                        val data = response.data
                        AccountManager.loginSuccess(data!!)
//                        setResult(Activity.RESULT_OK, Intent())
                        finish()
                    } else{
                        showToast(getString(R.string.login_failed)+" "+response.msg)

                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showToast(getString(R.string.login_failed)+" "+throwable.message)
                }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((resultCode == Activity.RESULT_OK) and (data!=null) and (requestCode == REQUEST_CODE_REGISTRATION)){
            val username = data!!.getStringExtra("username")
            if (!TextUtils.isEmpty(username)){
                findViewById<InputItemLayout>(R.id.input_item_user_name).getEditText().setText(username)
            }
        }
    }

    private fun goRegister() {
        startActivityForResult(
            Intent(this,RegisterActivity::class.java),
            REQUEST_CODE_REGISTRATION
        )
//        ARouter.getInstance().build("/account/registration").navigation(this,REQUEST_CODE_REGISTRATION)
    }
}