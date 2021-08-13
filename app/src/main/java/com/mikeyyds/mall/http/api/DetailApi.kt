package com.mikeyyds.mall.http.api

import com.mikeyyds.library.restful.MikeCall
import com.mikeyyds.library.restful.annotation.Get
import com.mikeyyds.library.restful.annotation.Path
import com.mikeyyds.mall.model.DetailModel

interface DetailApi{
    @Get("goods/detail/{id}")
    fun queryDetail(@Path("id") goodsId:String): MikeCall<DetailModel>
}