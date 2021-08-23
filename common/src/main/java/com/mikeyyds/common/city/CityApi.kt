package com.mikeyyds.common.city

import com.mikeyyds.library.restful.MikeCall
import com.mikeyyds.library.restful.annotation.Get

interface CityApi {
    @Get("cities")
    fun listCities(): MikeCall<CityModel>
}