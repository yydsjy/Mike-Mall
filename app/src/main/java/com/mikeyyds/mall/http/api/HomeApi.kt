package com.mikeyyds.mall.http.api

import com.mikeyyds.library.restful.MikeCall
import com.mikeyyds.library.restful.annotation.CacheStrategy
import com.mikeyyds.library.restful.annotation.Field
import com.mikeyyds.library.restful.annotation.Get
import com.mikeyyds.library.restful.annotation.Path
import com.mikeyyds.mall.model.HomeModel
import com.mikeyyds.mall.model.TabCategory

interface HomeApi {
    @CacheStrategy(CacheStrategy.CACHE_FIRST)
    @Get("category/categories")
    fun querryTabList(): MikeCall<List<TabCategory>>

    @Get("home/{categoryId}")
    fun querryTabCategoryList(
        @CacheStrategy cacheStrategy: Int,
        @Path("categoryId") categoryId: String,
        @Field("pageIndex") pageIndex: Int,
        @Field("pageSize") pageSize: Int
    ): MikeCall<HomeModel>
}