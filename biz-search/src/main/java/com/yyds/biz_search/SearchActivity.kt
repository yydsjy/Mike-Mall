package com.yyds.biz_search

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.mikeyyds.common.ui.component.MikeBaseActivity
import com.mikeyyds.common.ui.view.EmptyView
import com.mikeyyds.library.util.MikeDisplayUtil
import com.mikeyyds.library.util.MikeRes
import com.mikeyyds.library.util.MikeStatusBar
import com.mikeyyds.ui.search.MikeSearchView
import com.mikeyyds.ui.search.SimpleTextWacther
import com.mikeyyds.ui.title.MikeNavigationBar
import com.yyds.biz_search.SearchViewModel.Companion.PAGE_INIT_INDEX
import com.yyds.biz_search.view.GoodsSearchView
import com.yyds.biz_search.view.HistorySearchView
import com.yyds.biz_search.view.QuickSearchView

@Route(path = "/search/main")
class SearchActivity : MikeBaseActivity() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchButton: Button
    private lateinit var searchView: MikeSearchView
    private var emptyView: EmptyView? = null
    private var quickSearchView: QuickSearchView? = null
    private var goodsSearchView: GoodsSearchView? = null
    private var historyView: HistorySearchView? = null

    private var status = -1

    companion object {
        const val STATUS_EMPTY = 0
        const val STATUS_HISTORY = 1
        const val STATUS_QUICK_SEARCH = 2
        const val STATUS_GOODS_SEARCH = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MikeStatusBar.setStatusBar(this, true, translucent = false)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        initTopBar()
        updateViewStatus(STATUS_EMPTY)
        queryLocalHistory()
    }

    private fun queryLocalHistory() {
        viewModel.queryLocalHistory().observe(this, Observer { keywords ->
            if (keywords != null) {
                updateViewStatus(STATUS_HISTORY)
                historyView?.bindData(keywords)
            }
        })
    }

    private fun updateViewStatus(newStatus: Int) {
        if (status == newStatus) return
        status = newStatus

        var showView: View? = null
        when (status) {
            STATUS_EMPTY -> {
                if (emptyView == null) {
                    emptyView = EmptyView(this)
                    emptyView?.setDesc(MikeRes.getString(R.string.list_empty_desc))
                    emptyView?.setIcon(R.string.list_empty)
                }
                showView = emptyView
            }

            STATUS_QUICK_SEARCH -> {
                if (quickSearchView == null) {
                    quickSearchView = QuickSearchView(this)
                }
                showView = quickSearchView
            }

            STATUS_GOODS_SEARCH -> {
                if (goodsSearchView == null) {
                    goodsSearchView = GoodsSearchView(this)
                    goodsSearchView?.enableLoadMore(5) {
                        val keyword = searchView.getKeyword()
                        if (TextUtils.isEmpty(keyword)) return@enableLoadMore
                        viewModel.goodsSearch(keyword!!, false)
                    }
                }
                showView = goodsSearchView
            }

            STATUS_HISTORY -> {
                if (historyView == null) {
                    historyView = HistorySearchView(this)
                    historyView!!.setOnCheckedChangedListener {
                        dokwSearch(it)
                    }
                    historyView!!.setOnHistoryClearListener {
                        viewModel.clearHistory()
                        updateViewStatus(STATUS_EMPTY)
                    }
                }
                showView = historyView

            }
        }

        val container = findViewById<FrameLayout>(R.id.container)

        if (showView != null) {
            if (showView.parent == null) {
                container.addView(showView)
            }
            val childCount = container.childCount
            for (index in 0 until childCount) {
                val child = container.getChildAt(index)
                child.visibility = if (child == showView) View.VISIBLE else View.GONE
            }


        }
    }

    private fun initTopBar() {
        val navigationBar = findViewById<MikeNavigationBar>(R.id.nav_bar)
        navigationBar.setNavListener(View.OnClickListener { onBackPressed() })
        searchButton =
            navigationBar.addRightTextButton(R.string.nav_item_search, R.id.id_nav_item_search)
        searchButton.setTextColor(MikeRes.getColorStateList(R.color.color_nav_item_search))
        searchButton.setOnClickListener(searchClickListener)
        searchButton.isEnabled = false

        searchView = MikeSearchView(this)
        searchView.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            MikeDisplayUtil.dp2px(38f)
        )
        searchView.setHintText(MikeRes.getString(R.string.search_hint))
        searchView.setClearIconClickListener(updateHistoryListener)
        searchView.setDebounceTextChangeListener(debounceTextWatcher)
        navigationBar.addCenterView(searchView)
    }

    private val searchClickListener = View.OnClickListener {
        val keyword = searchView.editText?.text?.trim().toString()
        if (TextUtils.isEmpty(keyword)) return@OnClickListener
        dokwSearch(Keyword(null, keyword))
    }

    private val updateHistoryListener = View.OnClickListener {
        if (viewModel.keywords.isNullOrEmpty()) {
            updateViewStatus(STATUS_EMPTY)
        } else {
            updateViewStatus(STATUS_HISTORY)
            historyView?.bindData(viewModel.keywords!!)
        }
    }

    private val debounceTextWatcher = object : SimpleTextWacther() {
        override fun afterTextChanged(s: Editable?) {
            val hasContent = s != null && s.trim().isNotEmpty()
            searchButton.isEnabled = hasContent
            if (hasContent) {
                val liveData = viewModel.quickSearch(s.toString())
                Log.e("TAG", "afterTextChanged: "+liveData.hashCode() )
                liveData.observe(this@SearchActivity, Observer { list ->
                    if (list.isNullOrEmpty()) {
                        updateViewStatus(STATUS_EMPTY)
                    } else {
                        updateViewStatus(STATUS_QUICK_SEARCH)
                        quickSearchView?.bindData(list) { keyword ->
                            dokwSearch(keyword)
                        }
                    }
                })
            }
        }
    }

    private fun dokwSearch(keyword: Keyword) {
        searchView.setKeyWord(keyword.keyWord, updateHistoryListener)
        viewModel.saveHistory(keyword)
        val kwClearIconView = searchView.findViewById<View>(R.id.id_search_clear_icon)
        kwClearIconView.isEnabled = false
        viewModel.goodsSearch(keyword.keyWord, true)
        // TODO: 2021-08-16
        if (!viewModel.goodsSearchListliveData.hasObservers()) {

            viewModel.goodsSearchListliveData.observe(this, Observer { goodsList ->
                kwClearIconView?.isEnabled = true
                goodsSearchView?.loadFinished(!goodsList.isNullOrEmpty())
                if (goodsList == null) {
                    if (viewModel.pageIndex == PAGE_INIT_INDEX) {
                        updateViewStatus(STATUS_EMPTY)
                    }
                } else {
                    updateViewStatus(STATUS_GOODS_SEARCH)
                    goodsSearchView?.bindData(goodsList, viewModel.pageIndex == PAGE_INIT_INDEX + 1)
                }
            })
        }


    }
}