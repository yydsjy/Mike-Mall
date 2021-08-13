package com.mikeyyds.mall.model

import java.io.Serializable

data class CourseNotice(val total: Int, val list: List<Notice>?)

data class Notice(
    val id: String,
    val sticky: Int,
    val type: String,
    val title: String,
    val subtitle: String,
    val url: String,
    val cover: String,
    val createTime: String
):Serializable

data class UserProfile(
    val isLogin: Boolean,
    val favoriteCount: Int,
    val browseCount: Int,
    val learnMinutes: Int,
    val userName: String,
    val avatar: String,
    val bannerNoticeList: List<Notice>
):Serializable