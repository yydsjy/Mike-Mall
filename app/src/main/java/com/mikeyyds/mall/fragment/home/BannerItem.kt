package com.mikeyyds.mall.fragment.home

import android.graphics.Color
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mikeyyds.common.ui.view.loadUrl
import com.mikeyyds.library.util.MikeDisplayUtil
import com.mikeyyds.mall.model.HomeBanner
import com.mikeyyds.mall.route.MikeRoute
import com.mikeyyds.ui.banner.MikeBanner
import com.mikeyyds.ui.banner.core.MikeBannerMo
import com.mikeyyds.ui.item.core.MikeDataItem

class BannerItem(val list: List<HomeBanner>) :
    MikeDataItem<List<HomeBanner>, RecyclerView.ViewHolder>(list) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        val banner = holder.itemView as MikeBanner
        val models = mutableListOf<MikeBannerMo>()
        list.forEachIndexed { index, homeBanner ->
            val bannerMo = object : MikeBannerMo() {}
            bannerMo.url = homeBanner.cover
            models.add(bannerMo)
        }
        banner.setBannerData(models)
        banner.setOnBannerClickListener { viewHolder, bannerMo, position ->
            MikeRoute.startActivity4Browser(list[position].url)
        }

        banner.setBindAdapter { viewHolder, mo, position ->
            ((viewHolder.rootView) as ImageView).loadUrl(mo.url)
        }
    }

    override fun getItemView(parent: ViewGroup): View? {
        val context = parent.context
        val banner = MikeBanner(context)
        val params = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            MikeDisplayUtil.dp2px(160f)
        )
        params.bottomMargin = MikeDisplayUtil.dp2px(10f)
        banner.layoutParams = params
        banner.setBackgroundColor(Color.WHITE)
        return banner
    }

}