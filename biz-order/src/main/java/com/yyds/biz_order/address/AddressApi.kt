package com.yyds.biz_order.address

import com.mikeyyds.library.restful.MikeCall
import com.mikeyyds.library.restful.annotation.Field
import com.mikeyyds.library.restful.annotation.Get
import com.mikeyyds.library.restful.annotation.Path
import com.mikeyyds.library.restful.annotation.Post
import org.devio.hi.library.restful.annotation.Delete
import org.devio.hi.library.restful.annotation.Put

interface AddressApi {
    @Get("address")
    fun querryAddress(
        @Field("pageIndex") pageIndex: Int,
        @Field("pageSize") pageSize: Int
    ): MikeCall<AddressModel>

    @Post("address", formPost = false)
    fun addAddress(
        @Field("province") province: String,
        @Field("city") city: String,
        @Field("area") area: String,
        @Field("detail") detail:String,
        @Field("receiver") receiver: String,
        @Field("phoneNum") phoneNum: String
    ): MikeCall<String>

    @Put("address/{id}", formPost = false)
    fun updateAddress(
        @Path("id") id: String,
        @Field("province") province: String,
        @Field("city") city: String,
        @Field("area") area: String,
        @Field("detail") detail:String,
        @Field("receiver") receiver: String,
        @Field("phoneNum") phoneNum: String
    ): MikeCall<String>

    @Delete("address/{id}")
    fun deleteAddress(@Path("id") id: String): MikeCall<String>
}