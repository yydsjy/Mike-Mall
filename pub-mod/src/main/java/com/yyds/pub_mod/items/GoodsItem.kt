package com.yyds.pub_mod.items

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mikeyyds.common.route.MikeRoute
import com.mikeyyds.library.util.MikeDisplayUtil
import com.yyds.pub_mod.BR
import com.mikeyyds.ui.item.core.MikeDataItem
import com.yyds.pub_mod.R
import com.yyds.pub_mod.model.GoodsModel

open class GoodsItem(val goodsModel: GoodsModel, val isHotTab: Boolean) :
    MikeDataItem<GoodsModel, RecyclerView.ViewHolder>(goodsModel) {

    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
/*        holder.itemView.findViewById<ImageView>(R.id.item_image).loadUrl(goodsModel.sliderImage)
        holder.itemView.findViewById<TextView>(R.id.item_title).text = goodsModel.goodsName
        holder.itemView.findViewById<TextView>(R.id.item_price).text = selectPrice(goodsModel.groupPrice,goodsModel.marketPrice)
        holder.itemView.findViewById<TextView>(R.id.item_desc).text = goodsModel.completedNumText*/
        (holder as GoodsItemViewHolder).dataBinding.setVariable(BR.goodsModel,goodsModel)


        val itemLabelContainer =
            holder.itemView.findViewById<LinearLayout>(R.id.item_label_container)

        if (itemLabelContainer!=null) {
            if (!TextUtils.isEmpty(goodsModel.tags)) {
                itemLabelContainer.visibility = View.VISIBLE
                val split = goodsModel.tags!!.split(" ")
                for (index in split.indices) {
                    val labelView = if (index > itemLabelContainer.childCount - 1) {
                        val view = createLabelView(context, index != 0)
                        itemLabelContainer.addView(view)
                        view
                    } else {
                        itemLabelContainer.getChildAt(index) as TextView
                    }
                    labelView.text = split[index]
                }
            } else {
                itemLabelContainer.visibility = View.GONE
            }
        }

        if (!isHotTab) {
            val margin = MikeDisplayUtil.dp2px(2f)
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            val parentLeft = mikeAdapter?.getAttachRecyclerView()?.left ?: 0
            val parentPaddingLeft = mikeAdapter?.getAttachRecyclerView()?.paddingLeft ?: 0
            val itemLeft = holder.itemView.left

            if (itemLeft == (parentLeft + parentPaddingLeft)) {
                params.rightMargin = margin
            } else {
                params.leftMargin = margin
            }

            holder.itemView.layoutParams = params
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("goodsId", goodsModel.goodsId)
            bundle.putParcelable("goodsModel", goodsModel)
            MikeRoute.startActivity(context, bundle, MikeRoute.Destination.DETAIL_MAIN)
        }

    }

    fun createLabelView(context: Context, withLeftMargin: Boolean): TextView {
        val labelView = TextView(context)
        labelView.setTextColor(ContextCompat.getColor(context, R.color.color_EED))
        labelView.textSize = 11f
        labelView.gravity = Gravity.CENTER
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            MikeDisplayUtil.dp2px(14f)
        )
        params.leftMargin = if (withLeftMargin) MikeDisplayUtil.dp2px(5f) else 0
        labelView.layoutParams = params
        return labelView

    }

//    override fun getItemView(parent: ViewGroup): View? {
//        val inflater= LayoutInflater.from(parent.context)
//        dataBinding =
//            DataBindingUtil.inflate<ViewDataBinding>(inflater, getItemLayoutRes(), parent, false)
//        return dataBinding!!.root
//    }

    override fun getItemLayoutRes(): Int {
        return if (isHotTab) R.layout.layout_home_goods_list_item1 else R.layout.layout_home_goods_list_item2
    }

    override fun getSpanSize(): Int {
        return if (isHotTab) super.getSpanSize() else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup): GoodsItemViewHolder? {
        val inflater= LayoutInflater.from(parent.context)
        val dataBinding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, getItemLayoutRes(), parent, false)
        return GoodsItemViewHolder(dataBinding)
    }

    class GoodsItemViewHolder(val dataBinding: ViewDataBinding):RecyclerView.ViewHolder(dataBinding.root)


}