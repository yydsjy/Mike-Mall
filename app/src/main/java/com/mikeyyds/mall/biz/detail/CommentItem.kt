package com.mikeyyds.mall.biz.detail

import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mikeyyds.common.ui.view.loadCircle
import com.mikeyyds.library.util.MikeDisplayUtil
import com.mikeyyds.mall.R
import com.mikeyyds.mall.model.DetailModel
import com.mikeyyds.ui.item.core.MikeDataItem
import kotlin.math.min

class CommentItem(val model:DetailModel):MikeDataItem<DetailModel,RecyclerView.ViewHolder>() {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context?:return
        holder.itemView.findViewById<TextView>(R.id.comment_title).text = model.commentCountTitle
        val commentTag = model.commentTags
        if (commentTag!=null){
            val tagArray = commentTag.split(" ")
            val chipGroup = holder.itemView.findViewById<ChipGroup>(R.id.chip_group)
            if (tagArray!=null&&tagArray.isNotEmpty()){
                for (index in tagArray.indices){
                    val chipLabel = if (index<chipGroup.childCount){
                        chipGroup.getChildAt(index) as Chip
                    } else {
                        val chipLabel = Chip(context)

                        chipLabel.chipCornerRadius = MikeDisplayUtil.dp2px(4f).toFloat()
                        chipLabel.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.color_FAF0))
                        chipLabel.setTextColor(ContextCompat.getColor(context,R.color.color_999))
                        chipLabel.textSize = 14f
                        chipLabel.gravity = Gravity.CENTER
                        chipLabel.isCheckedIconVisible=false
                        chipLabel.isCheckable=true
                        chipLabel.isChipIconVisible = false

                        chipGroup.addView(chipLabel)
                        chipLabel
                    }
                    chipLabel.text = tagArray[index]
                }
            }
        }
        model.commentModels?.let {
            val commentContainer = holder.itemView.findViewById<LinearLayout>(R.id.comment_container)
            for (index in 0..min(it.size-1,3)){
                val comment = it[index]
                val itemView = if (index<commentContainer.childCount){
                    commentContainer.getChildAt(index)
                } else{
                    val view = LayoutInflater.from(context).inflate(R.layout.layout_detail_comment_item,commentContainer,false)
                    commentContainer.addView(view)
                    view
                }
                itemView.findViewById<ImageView>(R.id.user_avatar).loadCircle(comment.avatar)
                itemView.findViewById<TextView>(R.id.user_name).text = comment.nickName
                itemView.findViewById<TextView>(R.id.comment_content).text = comment.content
            }
        }
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_comment
    }
}