package com.mikeyyds.mall.fragment.category

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.SparseIntArray
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.mikeyyds.common.ui.component.MikeBaseFragment
import com.mikeyyds.common.ui.view.EmptyView
import com.mikeyyds.common.ui.view.loadUrl
import com.mikeyyds.library.restful.MikeCallback
import com.mikeyyds.library.restful.MikeResponse
import com.mikeyyds.mall.R
import com.mikeyyds.mall.http.ApiFactory
import com.mikeyyds.mall.http.api.CategoryApi
import com.mikeyyds.mall.model.Subcategory
import com.mikeyyds.mall.model.TabCategory
import com.mikeyyds.mall.route.MikeRoute
import com.mikeyyds.ui.slider.MikeSliderView
import com.mikeyyds.ui.tab.bottom.MikeTabBottomLayout

class CategoryFragment : MikeBaseFragment() {
    private var emptyView: EmptyView? = null
    private val SPAN_COUNT = 3
    private val subcategoryListCache = mutableMapOf<String, List<Subcategory>>()
    private val decoration = CategoryItemDecoration({ position ->
        subcategoryList[position].groupName
    }, SPAN_COUNT)
    private val subcategoryList = mutableListOf<Subcategory>()
    private val groupSpanSizeOffset = SparseIntArray()
    private val spanSizeLookUp = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            var spanSize = 1
            val groupName = subcategoryList[position].groupName
            val nextGroupName =
                if (position + 1 < subcategoryList.size) subcategoryList[position + 1].groupName else null
            if (TextUtils.equals(groupName, nextGroupName)) {
                spanSize = 1
            } else {
                val indexOfKey = groupSpanSizeOffset.indexOfKey(position)
                val size = groupSpanSizeOffset.size()
                val lastGroupOffset = if (size <= 0) 0
                else if (indexOfKey >= 0) {
                    if (indexOfKey == 0) 0 else groupSpanSizeOffset.valueAt(indexOfKey - 1)
                } else {
                    groupSpanSizeOffset.valueAt(size - 1)
                }

                spanSize = SPAN_COUNT - (position + lastGroupOffset) % SPAN_COUNT
                if (indexOfKey < 0) {
                    val groupOffSet = lastGroupOffset + spanSize - 1
                    groupSpanSizeOffset.put(position, groupOffSet)
                }
            }
            return spanSize
        }

    }

    private val layoutManager = GridLayoutManager(context, SPAN_COUNT)

    override fun getLayouId(): Int {
        return R.layout.fragment_category
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MikeTabBottomLayout.clipBottomPadding(layoutView.findViewById(R.id.root_container))
        queryCategoryList()

    }

    private fun queryCategoryList() {
        ApiFactory.create(CategoryApi::class.java).queryCategoryList()
            .enqueue(object : MikeCallback<List<TabCategory>> {
                override fun onSuccess(response: MikeResponse<List<TabCategory>>) {
                    if (response.successful() && response.data != null) {
                        onQueryCategoryListSuccess(response.data!!)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showEmptyView()
                }

            })
    }

    private fun onQueryCategoryListSuccess(data: List<TabCategory>) {
        if (!isAlive) return
        emptyView?.visibility = View.GONE
        layoutView.findViewById<MikeSliderView>(R.id.slider_view).visibility = View.VISIBLE

        layoutView.findViewById<MikeSliderView>(R.id.slider_view)
            .bindMenuView(itemCount = data.size, onBindView = { holder, position ->
                val category = data.get(position)
                holder.itemView.findViewById<TextView>(R.id.menu_item_title)?.text =
                    category.categoryName

            }, onItemClick = { holder, position ->
                val category = data.get(position)
                val categoryId = category.categoryId
                if (subcategoryListCache.containsKey(categoryId)) {
                    onQuerySubCategoryListSuccess(subcategoryListCache[categoryId]!!)
                } else {
                    querySubCategoryList(category.categoryId)
                }
            })


    }

    private fun querySubCategoryList(categoryId: String) {
        ApiFactory.create(CategoryApi::class.java).querySubCategoryList(categoryId)
            .enqueue(object : MikeCallback<List<Subcategory>> {
                override fun onSuccess(response: MikeResponse<List<Subcategory>>) {
                    if (response.successful() && response.data != null) {
                        onQuerySubCategoryListSuccess(response.data!!)
                    }
                    if (!subcategoryListCache.containsKey(categoryId)) {
                        subcategoryListCache[categoryId] = response.data!!
                    }
                }

                override fun onFailed(throwable: Throwable) {

                }

            })
    }


    private fun onQuerySubCategoryListSuccess(data: List<Subcategory>) {
        if (!isAlive) return
        decoration.clear()
        groupSpanSizeOffset.clear()
        subcategoryList.clear()
        subcategoryList.addAll(data)

        if (layoutManager.spanSizeLookup != spanSizeLookUp) {
            layoutManager.spanSizeLookup = spanSizeLookUp
        }
        layoutView.findViewById<MikeSliderView>(R.id.slider_view).bindContentView(
            itemCount = data.size,
            layoutManager = layoutManager,
            itemDecoration = decoration,
            onBindView = { holder, position ->
                val subcategory = data[position]
                holder.itemView.findViewById<ImageView>(R.id.content_item_image)
                    ?.loadUrl(subcategory.subcategoryIcon)
                holder.itemView.findViewById<TextView>(R.id.content_item_title)?.text =
                    subcategory.subcategoryName

            },
            onItemClick = { holder, position ->
                val subcategory = data[position]
                val bundle = Bundle()
                bundle.putString("categoryId", subcategory.categoryId)
                bundle.putString("subcategoryId", subcategory.subcategoryId)
                bundle.putString("categoryTitle",subcategory.subcategoryName)
                MikeRoute.startActivity(context!!,bundle,MikeRoute.Destination.GOODS_LIST)
            }
        )
    }

    private fun showEmptyView() {
        if (!isAlive) return
        if (emptyView == null) {
            emptyView = EmptyView(context!!)
            emptyView?.setIcon(R.string.if_empty_category)
            emptyView?.setDesc(getString(R.string.list_empty_desc))
            emptyView?.setButton(getString(R.string.list_empty_action), View.OnClickListener {
                queryCategoryList()
            })

            emptyView?.setBackgroundColor(Color.WHITE)
            emptyView?.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            layoutView.findViewById<LinearLayout>(R.id.root_container).addView(emptyView)

        }
        layoutView.findViewById<MikeSliderView>(R.id.slider_view).visibility = View.GONE
        emptyView?.visibility = View.VISIBLE
    }

}