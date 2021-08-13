package com.mikeyyds.mall.biz.detail

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.mikeyyds.library.util.ColorUtil
import com.mikeyyds.library.util.MikeDisplayUtil
import kotlin.math.abs
import kotlin.math.min

class TitleScrollListener(val callback: (Int) -> Unit, thresholdDp: Float = 100f) :
    RecyclerView.OnScrollListener() {
    private val thresholdPx = MikeDisplayUtil.dp2px(thresholdDp)
    private var lastFraction = 0f

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val viewHeader = recyclerView.findViewHolderForAdapterPosition(0) ?: return
        val top = abs(viewHeader.itemView.top).toFloat()

        val fraction = top / thresholdPx

        if (lastFraction > 1f) {
            lastFraction = fraction
            return
        }

        val newColor = ColorUtil.getCurrentColor(Color.TRANSPARENT, Color.WHITE, min(fraction, 1f))
        callback(newColor)

        lastFraction = fraction

    }

}