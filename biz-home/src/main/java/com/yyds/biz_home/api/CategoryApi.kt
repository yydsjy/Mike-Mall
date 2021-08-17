package com.yyds.biz_home.api

import com.mikeyyds.library.restful.MikeCall
import com.mikeyyds.library.restful.annotation.Get
import com.mikeyyds.library.restful.annotation.Path
import com.yyds.biz_home.model.Subcategory
import com.yyds.biz_home.model.TabCategory


interface CategoryApi{
    @Get("category/categories")
    fun queryCategoryList(): MikeCall<List<TabCategory>>

    @Get("category/subcategories/{categoryId}")
    fun querySubCategoryList(@Path("categoryId")categoryId:String):MikeCall<List<Subcategory>>
}