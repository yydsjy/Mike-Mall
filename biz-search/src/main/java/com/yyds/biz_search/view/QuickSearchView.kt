package com.yyds.biz_search.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikeyyds.ui.item.core.MikeAdapter
import com.mikeyyds.ui.item.core.MikeDataItem
import com.yyds.biz_search.Keyword
import com.yyds.biz_search.R

class QuickSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = MikeAdapter(context)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    fun bindData(list: List<Keyword>, callback: (Keyword) -> Unit) {
        val dataItems = mutableListOf<QuickSearchItem>()
        for (keyword in list){
            dataItems.add(QuickSearchItem(keyword,callback))
        }
        val mikeAdapter = adapter as MikeAdapter
        mikeAdapter.clearItems()
        mikeAdapter.addItems(dataItems,false)
    }

    private inner class QuickSearchItem(val keyword: Keyword, val callback: (Keyword) -> Unit) :
        MikeDataItem<Keyword, RecyclerView.ViewHolder>() {
        override fun onBindData(holder: ViewHolder, position: Int) {
            holder.itemView.findViewById<TextView>(R.id.item_title).text = keyword.keyWord
            holder.itemView.setOnClickListener{
                callback(keyword)
            }
        }

        override fun getItemLayoutRes(): Int {
            return R.layout.layout_quick_search_list_item
        }

    }
}