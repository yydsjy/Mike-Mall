package com.yyds.biz_home.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikeyyds.library.util.MikeDisplayUtil

import com.mikeyyds.common.route.MikeRoute
import com.mikeyyds.ui.item.core.MikeDataItem
import com.yyds.biz_home.databinding.LayoutHomeOpGridItemBinding
import com.yyds.biz_home.model.Subcategory

class GridItem(val list: List<Subcategory>) :
    MikeDataItem<List<Subcategory>, RecyclerView.ViewHolder>(list) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        val gridView = holder.itemView as RecyclerView
        gridView.adapter =GridAdapter(context,list)
    }

    override fun getItemView(parent: ViewGroup): View? {
        val gridView = RecyclerView(parent.context)
        val params = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        params.bottomMargin = MikeDisplayUtil.dp2px(10f)
        gridView.layoutManager = GridLayoutManager(parent.context, 5)
        gridView.layoutParams = params
        gridView.setBackgroundColor(Color.WHITE)
        return gridView

    }

    inner class GridAdapter(val context: Context, val list: List<Subcategory>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private lateinit var dataBinding: LayoutHomeOpGridItemBinding
        private var inflater = LayoutInflater.from(context)


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//            val view = inflater.inflate(R.layout.layout_home_op_grid_item, parent, false)
            dataBinding = LayoutHomeOpGridItemBinding.inflate(inflater, parent, false)
            return object : RecyclerView.ViewHolder(dataBinding.root) {}
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val subcategory = list[position]
            dataBinding.subCategory = subcategory
//            holder.itemView.findViewById<ImageView>(R.id.item_image).loadUrl(subcategory.subcategoryIcon)
//            holder.itemView.findViewById<TextView>(R.id.item_title).text = subcategory.subcategoryName

            holder.itemView.setOnClickListener{
                val bundle = Bundle()
                bundle.putString("categoryId", subcategory.categoryId)
                bundle.putString("subcategoryId", subcategory.subcategoryId)
                bundle.putString("categoryTitle",subcategory.subcategoryName)
                MikeRoute.startActivity(context,bundle, MikeRoute.Destination.GOODS_LIST)
            }
        }

    }

}