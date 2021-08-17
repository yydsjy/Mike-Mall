package com.yyds.biz_detail.api

import com.mikeyyds.library.restful.MikeCall
import com.mikeyyds.library.restful.annotation.Get
import com.mikeyyds.library.restful.annotation.Path
import com.yyds.biz_detail.model.DetailModel


interface DetailApi{
    @Get("goods/detail/{id}")
    fun queryDetail(@Path("id") goodsId:String): MikeCall<DetailModel>
}