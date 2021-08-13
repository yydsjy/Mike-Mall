package com.mikeyyds.mall.biz.goods

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.mikeyyds.common.ui.component.MikeAbsListFragment
import com.mikeyyds.library.restful.MikeCallback
import com.mikeyyds.library.restful.MikeResponse
import com.mikeyyds.mall.fragment.home.GoodsItem
import com.mikeyyds.mall.http.ApiFactory
import com.mikeyyds.mall.http.api.GoodsApi
import com.mikeyyds.mall.model.GoodsList
import com.mikeyyds.mall.model.GoodsModel

class GoodsListFragment : MikeAbsListFragment() {

    @JvmField
    @Autowired
    var categoryTitle: String = ""

    @JvmField
    @Autowired
    var categoryId: String = ""

    @JvmField
    @Autowired
    var subcategoryId: String = ""

    companion object {
        fun newInstance(categoryId: String, subcategoryId: String): Fragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            args.putString("subcategoryId", subcategoryId)
            val fragment = GoodsListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ARouter.getInstance().inject(this)
        loadData()
        enableLoadMore { loadData() }
    }

    override fun onRefresh() {
        super.onRefresh()
        loadData()
    }

    private fun loadData() {
        ApiFactory.create(GoodsApi::class.java)
            .queryCategoryGoodsList(categoryId, subcategoryId, 10, pageIndex).enqueue(object:MikeCallback<GoodsList>{
                override fun onSuccess(response: MikeResponse<GoodsList>) {
                    if (response.successful()&&response.data!=null){
                        onQueryCategoryGoodsList(response.data!!)
                    } else {
                        finishRefresh(null)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    finishRefresh(null)
                }

            })
    }

    private fun onQueryCategoryGoodsList(data: GoodsList) {
        val dataItems = mutableListOf<GoodsItem>()
        for (goodsModel in data.list) {
            val goodsItem = GoodsItem(goodsModel,false)
            dataItems.add(goodsItem)
        }
        finishRefresh(dataItems)
    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context,2)
    }

}