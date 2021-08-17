package com.yyds.biz_detail.items

import androidx.recyclerview.widget.RecyclerView
import com.mikeyyds.ui.item.core.MikeDataItem
import com.yyds.biz_detail.R

class SimiliarTitleItem: MikeDataItem<Any, RecyclerView.ViewHolder>() {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_similiar_title
    }
}