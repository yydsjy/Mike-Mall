package com.yyds.biz_home.api
import com.mikeyyds.library.restful.MikeCall
import com.mikeyyds.library.restful.annotation.Get
import com.yyds.biz_home.model.CourseNotice

interface NoticeApi {
    @Get("notice")
    fun notice(): MikeCall<CourseNotice>
}