package com.yyds.biz_order

import android.text.TextUtils
import java.math.BigDecimal
import java.text.DecimalFormat

internal object PriceUtil {
    private fun subPrice(goodsPrice:String?):String?{
        if (goodsPrice!=null){
            if (goodsPrice.startsWith("¥")&&goodsPrice.length>1){
                return goodsPrice.substring(1,goodsPrice.length)
            }
            return goodsPrice
        }
        return null
    }

    fun calculate(goodsPrice: String?,amount:Int):String{
        val price = subPrice(goodsPrice)
        if (TextUtils.isEmpty(price)) return "";
        val bigDecimal = BigDecimal(price)
        val result = bigDecimal.multiply(BigDecimal(amount))
        val df = DecimalFormat("###,###.##")
        return df.format(result)


    }
}