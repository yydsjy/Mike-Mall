package com.yyds.biz_detail.items

import android.content.Context
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikeyyds.common.ui.view.loadUrl

import com.mikeyyds.ui.item.core.MikeAdapter
import com.mikeyyds.ui.item.core.MikeDataItem
import com.yyds.biz_detail.R
import com.yyds.biz_detail.model.DetailModel
import com.yyds.biz_detail.model.Shop
import com.yyds.pub_mod.items.GoodsItem
import com.yyds.pub_mod.model.GoodsModel

class ShopItem(val detailModel: DetailModel) :
    MikeDataItem<DetailModel, RecyclerView.ViewHolder>() {
    private val SHOP_GOODS_ITEM_SPAN_COUNT = 3
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        val shop: Shop? = detailModel.shop
        shop?.let {
            holder.itemView.findViewById<ImageView>(R.id.shop_logo).loadUrl(it.logo)
            holder.itemView.findViewById<TextView>(R.id.shop_title).text = it.name
            holder.itemView.findViewById<TextView>(R.id.shop_desc).text =
                String.format(context.getString(R.string.shop_desc), it.goodsNum, it.completedNum)
            val evaluation: String? = shop.evaluation
            evaluation?.let {
                val tagContainer = holder.itemView.findViewById<LinearLayout>(R.id.tag_container)
                tagContainer.visibility = View.VISIBLE
                val serviceTags = evaluation.split(" ")
                var index = 0
                for (tagIndex in 0..(serviceTags.size / 2-1)) {
                    val tagView: TextView = if (tagIndex < tagContainer.childCount) {
                        tagContainer.getChildAt(tagIndex) as TextView
                    } else {
                        val tagView = TextView(context)
                        val params =
                            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                        params.weight = 1f
                        tagView.layoutParams = params
                        tagView.gravity = Gravity.CENTER
                        tagView.textSize = 14f
                        tagView.setTextColor(ContextCompat.getColor(context, R.color.color_666))
                        tagContainer.addView(tagView)

                        tagView
                    }

                    val serviceName = serviceTags[index]
                    val serviceTag = serviceTags[index + 1]
                    index += 2

                    val spanTag = spanServiceTag(context, serviceName, serviceTag)
                    tagView.text = spanTag
                }
            }
        }
        val flowGoods: List<GoodsModel>? = detailModel.flowGoods
        flowGoods?.let {
            val flowRecyclerView: RecyclerView =
                holder.itemView.findViewById<RecyclerView>(R.id.flow_recycler_view)
            flowRecyclerView.visibility = View.VISIBLE
            if (flowRecyclerView.layoutManager == null) {
                flowRecyclerView.layoutManager =
                    GridLayoutManager(context, SHOP_GOODS_ITEM_SPAN_COUNT)
            }
            if (flowRecyclerView.adapter == null) {
                flowRecyclerView.adapter = MikeAdapter(context)
            }

            val dataItems = mutableListOf<GoodsItem>()
            it.forEach {
                dataItems.add(ShopGoodsItem(it))
            }
            val mikeAdapter = flowRecyclerView.adapter as MikeAdapter
            mikeAdapter.clearItems()
            mikeAdapter.addItems(dataItems, true)

        }

    }

    private inner class ShopGoodsItem(goodsModel: GoodsModel) : GoodsItem(goodsModel, false) {
        override fun getItemLayoutRes(): Int {
            return R.layout.layout_detail_shop_goods_item
        }

        override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
            super.onViewAttachedToWindow(holder)
            val viewParent: ViewGroup = holder.itemView.parent as ViewGroup
            val availableWidth =
                viewParent.measuredWidth - viewParent.paddingLeft - viewParent.paddingRight
            val itemWidth = availableWidth / SHOP_GOODS_ITEM_SPAN_COUNT

            val imageView = holder.itemView.findViewById<ImageView>(R.id.item_image)
            val params = imageView.layoutParams
            params.width = itemWidth
            params.height = itemWidth
            imageView.layoutParams = params
        }
    }

    private fun spanServiceTag(
        context: Context,
        serviceName: String,
        serviceTag: String
    ): CharSequence {
        val ss = SpannableString(serviceTag)
        val ssb = SpannableStringBuilder()
        ss.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_C61)), 0, ss.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ss.setSpan(
            BackgroundColorSpan(ContextCompat.getColor(context, R.color.color_F8E)),
            0,
            ss.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssb.append(serviceName+" ")
        ssb.append(ss)
        return ssb
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_shop
    }
}