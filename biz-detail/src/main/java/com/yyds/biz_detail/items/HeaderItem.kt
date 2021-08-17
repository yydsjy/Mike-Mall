package com.yyds.biz_detail.items

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikeyyds.common.ui.view.loadUrl
import com.yyds.biz_detail.model.DetailModel
import com.mikeyyds.ui.banner.MikeBanner
import com.mikeyyds.ui.banner.core.MikeBannerAdapter
import com.mikeyyds.ui.banner.core.MikeBannerMo
import com.mikeyyds.ui.banner.indicator.MikeNumberIndicator
import com.mikeyyds.ui.item.core.MikeDataItem
import com.yyds.biz_detail.R
import com.yyds.pub_mod.model.SliderImage

class HeaderItem(
    val sliderImages: List<SliderImage>?,
    val price: String?,
    val completedNumText: String?,
    val goodsName: String?
) : MikeDataItem<DetailModel, RecyclerView.ViewHolder>() {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context?:return
        val bannerItems =  arrayListOf<MikeBannerMo>()
        sliderImages?.forEach{
            val bannerMo = object : MikeBannerMo(){}
            bannerMo.url = it.url
            bannerItems.add(bannerMo)
        }
        holder.itemView.findViewById<MikeBanner>(R.id.mike_banner).setMikeIndicator(MikeNumberIndicator(context))
        holder.itemView.findViewById<MikeBanner>(R.id.mike_banner).setBannerData(bannerItems)
        holder.itemView.findViewById<MikeBanner>(R.id.mike_banner).setBindAdapter{viewHolder: MikeBannerAdapter.MikeBannerViewHolder?, mo: MikeBannerMo?, position: Int ->
            val imageView = viewHolder?.rootView as? ImageView
            mo.let { imageView?.loadUrl(mo!!.url) }
        }

        holder.itemView.findViewById<TextView>(R.id.price).text = spanPrice(price)
        holder.itemView.findViewById<TextView>(R.id.sale_desc).text = completedNumText
        holder.itemView.findViewById<TextView>(R.id.title).text = goodsName

    }

    private fun spanPrice(price: String?):CharSequence{
        if (TextUtils.isEmpty(price)) return ""
        val ss = SpannableString(price)
        ss.setSpan(AbsoluteSizeSpan(18,true),1,ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_header
    }


}