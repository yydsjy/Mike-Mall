package com.yyds.biz_search

import com.yyds.pub_mod.model.GoodsModel
import java.io.Serializable

data class QuickSearchList(
    val list:List<Keyword>,
    val total:Int
)

data class Keyword(
    val id:String?,
    val keyWord: String
):Serializable

data class GoodsSearchList(
    val total: Int,
    val list:List<GoodsModel>
)