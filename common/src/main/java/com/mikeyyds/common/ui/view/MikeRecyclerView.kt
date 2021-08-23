package com.mikeyyds.common.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mikeyyds.common.R
import com.mikeyyds.library.log.MikeLog
import com.mikeyyds.ui.item.core.MikeAdapter

open class MikeRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleArr: Int = 0
) : RecyclerView(context, attrs, defStyleArr) {

    private var footerView: View? = null
    private var isLoadingMore: Boolean = false
    private var loadMoreScrollListener: OnScrollListener? = null


    inner class LoadMoreScrollListener(val prefetchSize: Int, val callback: () -> Unit) :
        OnScrollListener() {
        val mikeAdapter = adapter as MikeAdapter
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (isLoadingMore) return
            val itemCount = mikeAdapter.itemCount
            if (itemCount <= 0) return
            val canScrollVertical = recyclerView.canScrollVertically(1)
            val lastVisibleItem = findLastVisibleItem(recyclerView)
            val firstVisibleItem = findFirstVisibleItem(recyclerView)
            if (lastVisibleItem <= 0) return
            val arriveBottom = lastVisibleItem >= itemCount - 1 && firstVisibleItem > 0
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING && (canScrollVertical || arriveBottom)) {
                addFooterView()
            }

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                return
            }

            val arrivePrefetchPosition = itemCount - lastVisibleItem <= prefetchSize
            if (!arrivePrefetchPosition) return

            isLoadingMore = true
            callback()

        }

        private fun findLastVisibleItem(recyclerView: RecyclerView): Int {
            when (val layoutManager = recyclerView.layoutManager) {
                is LinearLayoutManager -> {
                    return layoutManager.findLastVisibleItemPosition()
                }
                is StaggeredGridLayoutManager -> {
                    return layoutManager.findLastVisibleItemPositions(null)[0]
                }
            }
            return -1
        }

        private fun findFirstVisibleItem(recyclerView: RecyclerView): Int {
            when (val layoutManager = recyclerView.layoutManager) {
                is LinearLayoutManager -> {
                    return layoutManager.findFirstVisibleItemPosition()
                }
                is StaggeredGridLayoutManager -> {
                    return layoutManager.findFirstVisibleItemPositions(null)[0]
                }
            }
            return -1
        }


        private fun addFooterView() {
            val footerView = getFooterView()
            if (footerView.parent != null) {
                footerView.post {
                    addFooterView()
                }
            } else {
                mikeAdapter.addFooterView(footerView)
            }
        }

        private fun getFooterView(): View {
            if (footerView == null) {
                footerView = LayoutInflater.from(context)
                    .inflate(R.layout.layout_footer_loading, this@MikeRecyclerView, false)
            }
            return footerView!!
        }
    }

    fun enableLoadMore(prefetchSize: Int, callback: () -> Unit) {
        if (adapter !is MikeAdapter) {
            MikeLog.e("enableLoadMore must use mikeAdapter")
            return
        }

        loadMoreScrollListener = LoadMoreScrollListener(prefetchSize, callback)
        addOnScrollListener(loadMoreScrollListener!!)
    }

    fun disableLoadMore() {
        if (adapter !is MikeAdapter) {
            MikeLog.e("disableLoadMore must use mikeAdapter")
            return
        }

        val mikeAdapter = adapter as MikeAdapter
        footerView?.let {
            mikeAdapter.removeFooterView(footerView!!)
        }

        loadMoreScrollListener?.let {
            removeOnScrollListener(loadMoreScrollListener!!)
            loadMoreScrollListener = null
            footerView = null
            isLoadingMore = false
        }
    }

    fun isLoading(): Boolean {
        return isLoadingMore
    }

    fun loadFinished(success: Boolean) {
        if (adapter !is MikeAdapter) {
            MikeLog.e("loadFinished must use MikeAdapter")
            return
        }

        isLoadingMore = false
        val mikeAdapter = adapter as MikeAdapter
        if (!success) {
            footerView?.let {
                if (footerView!!.parent != null) {
                    mikeAdapter.removeFooterView(footerView!!)
                }
            }
        } else {

        }
    }

}