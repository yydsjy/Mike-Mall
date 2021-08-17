package com.yyds.biz_home.api

import com.mikeyyds.library.restful.MikeCall
import com.mikeyyds.library.restful.annotation.Field
import com.mikeyyds.library.restful.annotation.Get
import com.mikeyyds.library.restful.annotation.Path

import com.yyds.biz_home.model.GoodsList

interface GoodsApi {
    @Get("goods/goods/{categoryId}")
    fun queryCategoryGoodsList(
        @Path("categoryId") categoryId: String,
        @Field("subcategoryId") subcategoryId: String,
        @Field("pageSize")pageSize:Int,
        @Field("pageIndex") pageIndex:Int
    ): MikeCall<GoodsList>
}