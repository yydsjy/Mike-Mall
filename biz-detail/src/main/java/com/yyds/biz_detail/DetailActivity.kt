package com.yyds.biz_detail

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.mikeyyds.common.ui.component.MikeBaseActivity
import com.mikeyyds.common.ui.view.EmptyView
import com.mikeyyds.library.util.MikeStatusBar

import com.mikeyyds.common.route.MikeRoute
import com.mikeyyds.ui.icfont.IconFontTextView
import com.mikeyyds.ui.item.core.MikeAdapter
import com.mikeyyds.ui.item.core.MikeDataItem
import com.yyds.biz_detail.items.*
import com.yyds.biz_detail.model.DetailModel
import com.yyds.pub_mod.items.GoodsItem
import com.yyds.pub_mod.model.GoodsModel
import com.yyds.pub_mod.model.selectPrice
import com.yyds.service_login.LoginServiceProvider

@Route(path = "/detail/main")
class DetailActivity : MikeBaseActivity() {

    private lateinit var viewModel: DetailViewModel
    private var emptyView: EmptyView? = null

    @JvmField
    @Autowired
    var goodsId: String? = null

    @JvmField
    @Autowired
    var goodsModel: GoodsModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MikeStatusBar.setStatusBar(this, true, Color.TRANSPARENT, true)
        MikeRoute.inject(this)

        assert(!TextUtils.isEmpty(goodsId)) { "goodsId must not be null" }
        setContentView(R.layout.activity_detail)

        initView()
        preBindData()
        viewModel = DetailViewModel.get(goodsId, this)
        viewModel.queryDetailData().observe(this, Observer {
            if (it == null) {
                showEmptyView()
            } else {
                bindData(it!!)
            }
        })
    }

    private fun preBindData() {
        if (goodsModel == null) return
        val mikeAdapter = findViewById<RecyclerView>(R.id.recycler_view).adapter as MikeAdapter
        mikeAdapter.addItemAt(
            0, HeaderItem(
                goodsModel!!.sliderImages,
                selectPrice(goodsModel!!.groupPrice, goodsModel!!.marketPrice),
                goodsModel!!.completedNumText,
                goodsModel!!.goodsName
            ), false
        )

    }

    private fun bindData(detailModel: DetailModel) {
        findViewById<RecyclerView>(R.id.recycler_view).visibility = View.VISIBLE
        emptyView?.visibility = View.GONE
        val mikeAdapter = findViewById<RecyclerView>(R.id.recycler_view).adapter as MikeAdapter
        val dataItems = mutableListOf<MikeDataItem<*, RecyclerView.ViewHolder>>()
        dataItems.add(
            HeaderItem(
                detailModel.sliderImages,
                selectPrice(detailModel.groupPrice, detailModel.marketPrice),
                detailModel.completedNumText,
                detailModel.goodsName
            )
        )
        dataItems.add(
            CommentItem(detailModel)
        )

        dataItems.add(ShopItem(detailModel))

        dataItems.add(GoodsAttrItem(detailModel))

        detailModel.gallery?.forEach {
            dataItems.add(GalleryItem(it))
        }

        detailModel.similarGoods?.let {
            dataItems.add(SimiliarTitleItem())
            it.forEach {
                dataItems.add(GoodsItem(it, false))
            }
        }

        mikeAdapter.clearItems()
        mikeAdapter.addItems(dataItems, true)

        updateFavoriteActionFace(detailModel.isFavorite)
        updateOrderActionFace(detailModel)


    }

    private fun updateOrderActionFace(detailModel: DetailModel) {
        findViewById<TextView>(R.id.action_order).text = selectPrice(
            detailModel.groupPrice,
            detailModel.marketPrice
        ) + getString(R.string.detail_order_action)
    }

    private fun updateFavoriteActionFace(isFavorite: Boolean) {
        findViewById<IconFontTextView>(R.id.action_favorite).setOnClickListener {
            toggleFavorite()
        }
        findViewById<IconFontTextView>(R.id.action_favorite).setTextColor(
            ContextCompat.getColor(
                this,
                if (isFavorite) R.color.color_DD2 else R.color.color_999
            )
        )


    }

    private fun toggleFavorite() {
        if (!LoginServiceProvider.isLogin()) {
            LoginServiceProvider.login(this, Observer { loginSuccess ->
                if (loginSuccess)
                    toggleFavorite()
            })
        } else {
            findViewById<IconFontTextView>(R.id.action_favorite).isClickable = false
            viewModel.toggleFavorite().observe(this, Observer { success ->
                if (success != null) {
                    updateFavoriteActionFace(success)
                    val message =
                        if (success) getString(R.string.detail_favorite_success) else getString(
                            R.string.detail_cancel_favorite
                        )
                    showToast(message)
                } else {

                }
                findViewById<IconFontTextView>(R.id.action_favorite).isClickable = true
            })
        }
    }

    private fun showEmptyView() {
        if (emptyView == null) {
            emptyView = EmptyView(this)
            emptyView!!.setIcon(R.string.if_empty_detail)
            emptyView!!.setDesc(getString(R.string.list_empty_desc))
            // TODO: 2021-08-06
            emptyView!!.layoutParams = ConstraintLayout.LayoutParams(-1, -1)
            emptyView!!.setBackgroundColor(Color.WHITE)
            emptyView!!.setButton(getString(R.string.list_empty_action), View.OnClickListener {
                viewModel.queryDetailData()
            })
            findViewById<ConstraintLayout>(R.id.root_container).addView(emptyView)
        }

        findViewById<RecyclerView>(R.id.recycler_view).visibility = View.GONE
        emptyView!!.visibility = View.VISIBLE
    }

    private fun initView() {
        findViewById<TextView>(R.id.action_back).setOnClickListener { onBackPressed() }
        findViewById<RecyclerView>(R.id.recycler_view).layoutManager = GridLayoutManager(this, 2)
        findViewById<RecyclerView>(R.id.recycler_view).adapter = MikeAdapter(this)
        findViewById<RecyclerView>(R.id.recycler_view).addOnScrollListener((TitleScrollListener({
            findViewById<FrameLayout>(R.id.title_bar).setBackgroundColor(it)
        })))
    }

}