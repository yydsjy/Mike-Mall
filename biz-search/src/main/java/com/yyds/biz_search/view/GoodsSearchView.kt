package com.yyds.biz_search.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikeyyds.common.ui.view.MikeRecyclerView
import com.mikeyyds.ui.item.core.MikeAdapter
import com.yyds.pub_mod.items.GoodsItem
import com.yyds.pub_mod.model.GoodsModel

class GoodsSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MikeRecyclerView(context, attrs, defStyleAttr) {
    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = MikeAdapter(context)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    fun bindData(list: List<GoodsModel>, initLoad: Boolean) {
        val dataItems = mutableListOf<GoodsItem>()
        for (goodsModel in list) {
            dataItems.add(GoodsItem(goodsModel, true))
        }
        val mikeAdapter = adapter as MikeAdapter
        if (initLoad) mikeAdapter.clearItems()
        mikeAdapter.addItems(dataItems, true)
    }
}