package com.mikeyyds.mall.http.api

import com.mikeyyds.library.restful.MikeCall
import com.mikeyyds.library.restful.annotation.Path
import com.mikeyyds.library.restful.annotation.Post
import com.mikeyyds.mall.model.Favorite

interface FavoriteApi {

    @Post("favorites/{goodsId}")
    fun favorite(@Path("goodsId")goodsId:String): MikeCall<Favorite>
}