package com.mikeyyds.mall.http.api

import com.google.gson.JsonObject
import com.mikeyyds.library.restful.MikeCall
import com.mikeyyds.library.restful.annotation.Field
import com.mikeyyds.library.restful.annotation.Get
import com.mikeyyds.library.restful.annotation.Post

interface AccountApi {
    @Post("user/login")
    fun login(
        @Field("userName") userName: String,
        @Field("password") password: String
    ): MikeCall<String>

    @Post("user/registration")
    fun register(
        @Field("userName") userName: String,
        @Field("password") password: String,
        @Field("imoocId") imoocId:String,
        @Field("orderId") orderId:String
    ):MikeCall<String>
}