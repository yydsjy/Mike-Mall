package com.mikeyyds.common.http

import com.mikeyyds.common.utils.SPUtil
import com.mikeyyds.library.restful.MikeRestful

object ApiFactory{
    private val KEY_DEGRADE_HTTP = "degrade_http"
    private val HTTP_BASE_URL = "http://api.devio.org/as/"
    private val HTTPS_BASE_URL = "https://api.devio.org/as/"
    val degrade2Http = SPUtil.getBoolean(KEY_DEGRADE_HTTP)
    val baseUrl = if (degrade2Http) HTTP_BASE_URL else HTTPS_BASE_URL
    private val mikeRestful = MikeRestful(baseUrl, RetrofitCallFactory(baseUrl))

    init {
        mikeRestful.addInterceptor(BizInterceptor())
        mikeRestful.addInterceptor(HttpStatusInterceptor())

        SPUtil.putBoolean(KEY_DEGRADE_HTTP,false)
    }

    fun <T> create(service:Class<T>):T{
        return mikeRestful.create(service)
    }
}