package com.mikeyyds.common.ui.component

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikeyyds.common.R
import com.mikeyyds.common.ui.view.EmptyView
import com.mikeyyds.common.ui.view.MikeRecyclerView
import com.mikeyyds.ui.item.core.MikeAdapter
import com.mikeyyds.ui.item.core.MikeDataItem
import com.mikeyyds.ui.refresh.MikeOverView
import com.mikeyyds.ui.refresh.MikeRefresh
import com.mikeyyds.ui.refresh.MikeRefreshLayout
import com.mikeyyds.ui.refresh.MikeTextOverView

open class MikeAbsListFragment : MikeBaseFragment(), MikeRefresh.MikeRefreshListener {
    var pageIndex: Int = 1
    private lateinit var mikeAdapter: MikeAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var refreshHeaderView: MikeTextOverView
    private var loadingView: ContentLoadingProgressBar? = null
    private var emptyView: EmptyView? = null
    private var recyclerView: MikeRecyclerView? = null
    private var refreshLayout: MikeRefreshLayout? = null

    companion object {
        const val PREFETCH_SIZE = 5
    }

    override fun getLayouId(): Int {
        return R.layout.fragment_list
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout = layoutView.findViewById<MikeRefreshLayout>(R.id.refresh_layout)
        recyclerView = layoutView.findViewById<MikeRecyclerView>(R.id.recycler_view)
        emptyView = layoutView.findViewById<EmptyView>(R.id.empty_view)
        loadingView = layoutView.findViewById<ContentLoadingProgressBar>(R.id.content_laoding)

        refreshHeaderView = MikeTextOverView(context!!)
        refreshLayout?.setRefreshOverView(refreshHeaderView)
        refreshLayout?.setRefreshListener(this)

        layoutManager = createLayoutManager()
        mikeAdapter = MikeAdapter(context!!)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = mikeAdapter

        emptyView?.visibility = View.GONE
        emptyView?.setIcon(R.string.list_empty)
        emptyView?.setDesc(getString(R.string.list_empty_desc))
        emptyView?.setButton(getString(R.string.list_empty_action), View.OnClickListener {
            // TODO: 2021-08-01  
            onRefresh()
        })

        loadingView?.visibility = View.VISIBLE
        pageIndex = 1

    }

    fun finishRefresh(dataItems: List<MikeDataItem<*, RecyclerView.ViewHolder>>?) {
        val success = dataItems != null && dataItems.isNotEmpty()
        val refresh = pageIndex == 1
        if (refresh) {
            loadingView?.visibility = View.GONE
            refreshLayout?.refreshFinished()
            if (success) {
                emptyView?.visibility = View.GONE
                mikeAdapter.clearItems()
                mikeAdapter.addItems(dataItems!!, true)
            } else {
                if (mikeAdapter.itemCount <= 0) {
                    emptyView?.visibility = View.VISIBLE
                }
            }
        } else {
            if (success) {
                mikeAdapter.addItems(dataItems!!, true)
            }
            recyclerView?.loadFinished(success)
        }
    }

    fun enableLoadMore(callback: () -> Unit) {
        recyclerView?.enableLoadMore(PREFETCH_SIZE) {
            if (refreshHeaderView.state == MikeOverView.MikeRefreshState.STATE_REFRESH) {
                recyclerView?.loadFinished(false)
                return@enableLoadMore
            }
            pageIndex++
            callback()
        }
    }

    fun disableLoadMore() {
        recyclerView?.disableLoadMore()
    }

    open fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun enableRefresh(): Boolean {
        return true
    }

    @CallSuper
    override fun onRefresh() {
        if (recyclerView?.isLoading() == true) {
            refreshLayout?.post {
                refreshLayout?.refreshFinished()
            }
            return
        }
        pageIndex = 1
    }

}