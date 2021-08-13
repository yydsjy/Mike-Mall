package com.mikeyyds.mall.biz.detail

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikeyyds.common.ui.view.InputItemLayout
import com.mikeyyds.mall.R
import com.mikeyyds.mall.model.DetailModel
import com.mikeyyds.ui.item.core.MikeDataItem

class GoodsAttrItem(val detailModel: DetailModel) :
    MikeDataItem<DetailModel, RecyclerView.ViewHolder>() {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        val goodAttr = detailModel.goodAttr
        goodAttr?.let {
            val attrContainer = holder.itemView.findViewById<LinearLayout>(R.id.attr_container)
            attrContainer.visibility = View.VISIBLE
            var index = 0
            it.forEach {
                val entries = it.entries
                val key = entries.first().key
                val value = entries.first().value

                val attrItemView = if (index < attrContainer.childCount) {
                    attrContainer.getChildAt(index) as InputItemLayout
                } else {
                    val view = LayoutInflater.from(context)
                        .inflate(
                            R.layout.layout_detail_goods_attr_item,
                            attrContainer,
                            false
                        ) as InputItemLayout
                    attrContainer.addView(view)
                    view
                }

                attrItemView.getEditText().hint = value
                attrItemView.getEditText().isEnabled = false
                attrItemView.getTitleView().text = key
                index++
            }
        }

        detailModel.goodDescription?.let {
            val attrDesc = holder.itemView.findViewById<TextView>(R.id.attr_desc)
            attrDesc.visibility = View.VISIBLE
            attrDesc.text = it
        }


    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_goods_attr
    }
}