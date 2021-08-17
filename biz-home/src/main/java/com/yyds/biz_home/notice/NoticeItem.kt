package com.yyds.biz_home.notice

import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikeyyds.common.utils.DateUtil
import com.mikeyyds.library.util.MikeRes


import com.mikeyyds.common.route.MikeRoute
import com.mikeyyds.ui.item.core.MikeDataItem
import com.yyds.biz_home.R
import com.yyds.service_login.Notice

internal class NoticeItem(itemData: Notice): MikeDataItem<Notice, RecyclerView.ViewHolder>(itemData) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        mData?.apply {
            holder.itemView.findViewById<TextView>(R.id.tv_title).text = title
            if (TextUtils.equals(type,"goods")){
                holder.itemView.findViewById<TextView>(R.id.icon).text = MikeRes.getString(R.string.if_notice_recommend)
                val bundle = Bundle()
                bundle.putString("goodsId",url)
                holder.itemView.setOnClickListener{
                    MikeRoute.startActivity(context, bundle, MikeRoute.Destination.DETAIL_MAIN)
                }
            } else{
                holder.itemView.findViewById<TextView>(R.id.icon).text = MikeRes.getString(R.string.if_notice_msg)
                holder.itemView.setOnClickListener{
                    MikeRoute.startActivity4Browser(url)
                }
            }
            holder.itemView.findViewById<TextView>(R.id.tv_sub_title).text = subtitle
            holder.itemView.findViewById<TextView>(R.id.tv_publish_date).text =DateUtil.getMDDate(createTime)
        }
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_notice_item
    }
}