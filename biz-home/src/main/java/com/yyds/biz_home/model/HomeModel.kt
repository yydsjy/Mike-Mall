package com.yyds.biz_home.model

import com.yyds.pub_mod.model.GoodsModel
import com.yyds.service_login.Notice
import java.io.Serializable

data class HomeModel(
    val bannerList: List<HomeBanner>?,
    val subcategoryList: List<Subcategory>?,
    val goodsList: List<GoodsModel>?
):Serializable


data class TabCategory(val categoryId: String, val categoryName: String, val goodsCount: String):Serializable

data class HomeBanner(
    val cover: String,
    val createTime: String,
    val id: String,
    val sticky: Int,
    val subtitle: String,
    val title: String,
    val type: String,
    val url: String
):Serializable

data class Subcategory(
    val categoryId: String,
    val groupName: String,
    val showType: String,
    val subcategoryIcon: String,
    val subcategoryId: String,
    val subcategoryName: String
):Serializable


data class GoodsList(val total: Int, val list: List<GoodsModel>)

data class CourseNotice(val total: Int, val list: List<Notice>?)

