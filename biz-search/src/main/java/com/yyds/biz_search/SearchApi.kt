package com.yyds.biz_search

import com.mikeyyds.library.restful.MikeCall
import com.mikeyyds.library.restful.annotation.Field
import com.mikeyyds.library.restful.annotation.Get
import com.mikeyyds.library.restful.annotation.Post

interface SearchApi {
    @Get("goods/search/quick")
    fun quickSearch(@Field("keyWord") key: String): MikeCall<QuickSearchList>

    @Post("goods/search", formPost = false)
    fun goodsSearch(
        @Field("keyWord") keyword: String,
        @Field("pageIndex") pageIndex: Int,
        @Field("pageSize") pageSize: Int
    ) :MikeCall<GoodsSearchList>

}