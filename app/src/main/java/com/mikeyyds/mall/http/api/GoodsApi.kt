package com.mikeyyds.mall.http.api

import com.mikeyyds.library.restful.MikeCall
import com.mikeyyds.library.restful.annotation.Field
import com.mikeyyds.library.restful.annotation.Get
import com.mikeyyds.library.restful.annotation.Path
import com.mikeyyds.mall.model.GoodsList
import com.mikeyyds.mall.model.Subcategory

interface GoodsApi {
    @Get("goods/goods/{categoryId}")
    fun queryCategoryGoodsList(
        @Path("categoryId") categoryId: String,
        @Field("subcategoryId") subcategoryId: String,
        @Field("pageSize")pageSize:Int,
        @Field("pageIndex") pageIndex:Int
    ): MikeCall<GoodsList>
}