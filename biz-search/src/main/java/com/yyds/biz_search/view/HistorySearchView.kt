package com.yyds.biz_search.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.yyds.biz_search.Keyword
import com.yyds.biz_search.R
import java.util.ArrayList


class HistorySearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var chipGroup: ChipGroup? = null
    private val list = ArrayList<Keyword>()

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_history_search, this, true)
        orientation = LinearLayout.VERTICAL
        chipGroup = findViewById<ChipGroup>(R.id.chip_group)
    }

    fun bindData(keywords: ArrayList<Keyword>) {

        if (keywords == null) return
        list.clear()
        list.addAll(keywords)

        for (index in 0 until keywords.size) {
            var chipItem: Chip
            val childCount = chipGroup?.childCount
            if (index < childCount!!) {
                chipItem = chipGroup?.getChildAt(index) as Chip
            } else {
                chipItem = generateChipItem()
                chipGroup?.addView(chipItem)
            }

            chipItem.text = keywords[index].keyWord
        }

    }

    private fun generateChipItem(): Chip {
        val chipItem: Chip = LayoutInflater.from(context)
            .inflate(R.layout.layout_history_search_chip_item, chipGroup, false) as Chip
        chipItem.isCheckable = true
        chipItem.isChecked = false
        chipItem.id = chipGroup!!.childCount
        return chipItem
    }

    fun setOnCheckedChangedListener(callback: (Keyword) -> Unit) {
        chipGroup?.setOnCheckedChangeListener { chipGroup, checkedId ->
            for (index in 0 until chipGroup.childCount) {
                val checkBox = chipGroup.getChildAt(index) as AppCompatCheckBox
                if (checkBox.id == checkedId && checkBox.isChecked) {
                    callback(list[index])
                    chipGroup.clearCheck()
                    break
                }
            }
        }
    }

    fun setOnHistoryClearListener(callback: () -> Unit) {
        findViewById<TextView>(R.id.if_delete).setOnClickListener {
            chipGroup?.removeAllViews()
            list.clear()
            callback()

        }
    }
}