package com.yyds.biz_home.home

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikeyyds.common.ui.component.MikeAbsListFragment
import com.mikeyyds.library.restful.annotation.CacheStrategy

import com.mikeyyds.ui.item.core.MikeDataItem
import com.yyds.biz_home.model.HomeModel

class HomeTabFragment : MikeAbsListFragment() {
    private lateinit var viewModel: HomeViewModel
    private var categoryId: String? = null
    val DEFAULT_HOT_TAB_CATEGORY_ID = "1"

    companion object {
        fun newInstance(categoryId: String): HomeTabFragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            val fragment = HomeTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryId = arguments?.getString("categoryId", DEFAULT_HOT_TAB_CATEGORY_ID)
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        enableLoadMore { queryTabCategoryList(CacheStrategy.NET_ONLY) }
        queryTabCategoryList(CacheStrategy.CACHE_FIRST)
    }

    override fun onRefresh() {
        super.onRefresh()
        queryTabCategoryList(CacheStrategy.NET_CACHE)
    }

    private fun queryTabCategoryList(cacheStrategy: Int) {
        viewModel.queryTabCategoryList(categoryId, pageIndex, cacheStrategy)
            .observe(viewLifecycleOwner,
                Observer {
                    if (it != null) {
                        updateUI(it)
                    } else {
                        finishRefresh(null)
                    }
                })
    }

    private fun updateUI(data: HomeModel) {
        if (!isAlive) return
        val dataItems = mutableListOf<MikeDataItem<*, RecyclerView.ViewHolder>>()
        data.bannerList?.let {
            dataItems.add(BannerItem(data.bannerList))
        }

        data.subcategoryList?.let {
            dataItems.add(GridItem(data.subcategoryList))
        }

        data.goodsList?.forEachIndexed { index, goodsModel ->
            dataItems.add(
                com.yyds.pub_mod.items.GoodsItem(
                    goodsModel,
                    TextUtils.equals(categoryId, DEFAULT_HOT_TAB_CATEGORY_ID)
                )
            )
        }

        finishRefresh(dataItems)
    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        val isHotTab = TextUtils.equals(categoryId, DEFAULT_HOT_TAB_CATEGORY_ID)
        return if (isHotTab) LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        ) else GridLayoutManager(context, 2)
    }


}