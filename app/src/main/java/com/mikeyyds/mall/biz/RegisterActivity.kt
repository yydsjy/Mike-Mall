package com.mikeyyds.mall.biz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.mikeyyds.common.ui.component.MikeBaseActivity
import com.mikeyyds.common.ui.view.InputItemLayout
import com.mikeyyds.library.restful.MikeCallback
import com.mikeyyds.library.restful.MikeResponse
import com.mikeyyds.mall.R
import com.mikeyyds.mall.http.ApiFactory
import com.mikeyyds.mall.http.api.AccountApi

@Route(path = "/account/registration")
class RegisterActivity : MikeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        findViewById<TextView>(R.id.action_back).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.action_register).setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        val orderId =
            findViewById<InputItemLayout>(R.id.input_item_orderId).getEditText().text.toString()
        val mikeId =
            findViewById<InputItemLayout>(R.id.input_item_mikeId).getEditText().text.toString()
        val username =
            findViewById<InputItemLayout>(R.id.input_item_user_name).getEditText().text.toString()
        val pwd =
            findViewById<InputItemLayout>(R.id.input_item_password).getEditText().text.toString()
        val pwdConfirmed =
            findViewById<InputItemLayout>(R.id.input_item_confirm_password).getEditText().text.toString()

        if (TextUtils.isEmpty(orderId)
            or TextUtils.isEmpty(mikeId)
            or TextUtils.isEmpty(username)
            or TextUtils.isEmpty(pwd)
            or !TextUtils.equals(pwd, pwdConfirmed)
        ) {
            return
        }

        ApiFactory.create(AccountApi::class.java).register(username, pwd, mikeId, orderId)
            .enqueue(object : MikeCallback<String> {
                override fun onSuccess(response: MikeResponse<String>) {
                    if (response.code == MikeResponse.SUCCESS){
                        var intent = Intent()
                        intent.putExtra("username",username)
                        setResult(Activity.RESULT_OK,intent)
                        finish()
                    } else{
                        showToast(response.msg)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showToast(throwable.message)
                }

            })
    }
}