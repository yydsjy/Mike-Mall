package com.mikeyyds.mall.http.api

import com.mikeyyds.library.restful.MikeCall
import com.mikeyyds.library.restful.annotation.Get
import com.mikeyyds.library.restful.annotation.Path
import com.mikeyyds.mall.model.Subcategory
import com.mikeyyds.mall.model.TabCategory

interface CategoryApi{
    @Get("category/categories")
    fun queryCategoryList(): MikeCall<List<TabCategory>>

    @Get("category/subcategories/{categoryId}")
    fun querySubCategoryList(@Path("categoryId")categoryId:String):MikeCall<List<Subcategory>>
}