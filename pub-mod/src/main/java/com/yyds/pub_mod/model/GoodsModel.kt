package com.yyds.pub_mod.model

import android.os.Parcelable
import android.text.TextUtils
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class GoodsModel(
    val categoryId: String?,
    val completedNumText: String?,
    val createTime: String?,
    val goodsId: String?,
    val goodsName: String?,
    val groupPrice: String?,
    val hot: Boolean?,
    val joinedAvatars: List<SliderImage>?,
    val marketPrice: String?,
    val sliderImage: String?,
    val sliderImages: List<SliderImage>?,
    val tags: String?
) : Serializable, Parcelable

@Parcelize
data class SliderImage(
    val type: Int,
    val url: String
) : Serializable, Parcelable

fun selectPrice(groupPrice: String?, marketPrice: String?): String? {
    var price = if (TextUtils.isEmpty(marketPrice)) groupPrice else marketPrice
    if (price?.startsWith("¥") != true) {
        price = "¥".plus(price)
    }
    return price
}