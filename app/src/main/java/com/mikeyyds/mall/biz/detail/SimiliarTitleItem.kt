package com.mikeyyds.mall.biz.detail

import androidx.recyclerview.widget.RecyclerView
import com.mikeyyds.mall.R
import com.mikeyyds.ui.item.core.MikeDataItem

class SimiliarTitleItem: MikeDataItem<Any, RecyclerView.ViewHolder>() {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_similiar_title
    }
}