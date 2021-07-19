package com.mikeyyds.mall.http

import com.mikeyyds.library.restful.MikeRestful

object ApiFactory{
    private val baseUrl = "https://api.devio.org/as/"
    private val mikeRestful = MikeRestful(baseUrl,RetrofitCallFactory(baseUrl))

    init {
        mikeRestful.addInterceptor(BizInterceptor())
        mikeRestful.addInterceptor(HttpStatusInterceptor())
    }

    fun <T> create(service:Class<T>):T{
        return mikeRestful.create(service)
    }
}