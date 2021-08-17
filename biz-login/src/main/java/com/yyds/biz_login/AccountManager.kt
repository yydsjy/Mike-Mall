package com.yyds.biz_login

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mikeyyds.common.utils.SPUtil
import com.mikeyyds.library.cache.MikeStorage
import com.mikeyyds.library.executor.MikeExecutor
import com.mikeyyds.library.restful.MikeCallback
import com.mikeyyds.library.restful.MikeResponse

import com.mikeyyds.library.util.AppGlobals
import com.mikeyyds.common.http.ApiFactory
import com.yyds.service_login.UserProfile

import java.lang.IllegalStateException


object AccountManager {
    private var userProfile: UserProfile? = null
    private val KEY_BOARDING_PASS = "boarding-pass"
    private val KEY_USER_PROFILE = "user_profile"

    private var boardingPass: String? = null

    private val loginLiveData = MutableLiveData<Boolean>()
    private val loginForeverObservers = mutableListOf<Observer<Boolean>>()
    private val profileLiveData = MutableLiveData<UserProfile>()
    private val profileForeverObservers = mutableListOf<Observer<UserProfile?>>()

    @Volatile
    private var isFetching = false

    fun login(context: Context? = AppGlobals.get(), observer: Observer<Boolean>) {
        if (context is LifecycleOwner) {
            loginLiveData.observe(context, observer)
        } else {
            loginLiveData.observeForever(observer)
            loginForeverObservers.add(observer)
        }

        val intent = Intent(context, LoginActivity::class.java)
        if (context is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (context == null) {
            throw IllegalStateException("context can not be null")
        }

        context.startActivity(intent)
    }

    fun loginSuccess(boardingPass: String) {
        SPUtil.putString(KEY_BOARDING_PASS, boardingPass)
        AccountManager.boardingPass = boardingPass
        loginLiveData.value = true
        clearLoginForeverObservers()
    }

    private fun clearLoginForeverObservers() {
        for (observer in loginForeverObservers) {
            loginLiveData.removeObserver(observer)
        }
        loginForeverObservers.clear()
    }

    fun getBoardingPass(): String? {
        if (TextUtils.isEmpty(boardingPass)) {
            boardingPass = SPUtil.getString(KEY_BOARDING_PASS)
        }
        return boardingPass
    }

    @Synchronized
    fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile?>,
        onlyCache: Boolean = true
    ) {
        if (lifecycleOwner == null) {
            profileLiveData.observeForever(observer)
            profileForeverObservers.add(observer)
        } else {
            profileLiveData.observe(lifecycleOwner, observer)
        }

        if (userProfile != null && onlyCache) {
            profileLiveData.postValue(userProfile)
            return
        }

        if (isFetching) return
        isFetching = true
        ApiFactory.create(com.yyds.biz_login.api.AccountApi::class.java).profile()
            .enqueue(object : MikeCallback<UserProfile> {
                override fun onSuccess(response: MikeResponse<UserProfile>) {
                    userProfile = response.data
                    if (response.code == MikeResponse.SUCCESS && userProfile != null) {
                        MikeExecutor.execute(runnable = Runnable {
                            MikeStorage.saveCache(KEY_USER_PROFILE, userProfile)
                            isFetching = false
                        })

                        profileLiveData.value = userProfile
                    } else {
                        profileLiveData.value = null
                    }
                    clearProfileForeverObservers()

                }

                override fun onFailed(throwable: Throwable) {
                    isFetching = false
                    profileLiveData.value = null
                    clearProfileForeverObservers()
                }

            })
    }

    private fun clearProfileForeverObservers() {
        for (observer in profileForeverObservers) {
            profileLiveData.removeObserver(observer)
        }
        profileForeverObservers.clear()
    }

    fun isLogin(): Boolean {
        return !TextUtils.isEmpty(getBoardingPass())
    }

}