package com.mikeyyds.mall.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter
import com.mikeyyds.common.ui.component.MikeBaseFragment
import com.mikeyyds.common.ui.view.loadCircle
import com.mikeyyds.common.ui.view.loadCorner
import com.mikeyyds.library.restful.MikeCallback
import com.mikeyyds.library.restful.MikeResponse
import com.mikeyyds.library.util.MikeDisplayUtil
import com.mikeyyds.mall.R
import com.mikeyyds.mall.biz.account.AccountManager
import com.mikeyyds.mall.http.ApiFactory
import com.mikeyyds.mall.http.api.AccountApi
import com.mikeyyds.mall.model.CourseNotice
import com.mikeyyds.mall.model.Notice
import com.mikeyyds.mall.model.UserProfile
import com.mikeyyds.mall.route.MikeRoute
import com.mikeyyds.ui.banner.MikeBanner
import com.mikeyyds.ui.banner.core.MikeBannerAdapter
import com.mikeyyds.ui.banner.core.MikeBannerMo


class ProfileFragment : MikeBaseFragment() {
    private val REQUEST_CODE_LOGIN_PROFILE: Int = 1001
    val ITEM_PLACE_HOLDER = "      "
    var mikeBanner: MikeBanner? = null
    override fun getLayouId(): Int {
        return R.layout.fragment_profile_page
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val item_notification = view.findViewById<TextView>(R.id.item_notification)
        item_notification.setText(R.string.if_notify)
        item_notification.append(ITEM_PLACE_HOLDER + getString(R.string.item_notification))

        view.findViewById<LinearLayout>(R.id.ll_notice).setOnClickListener{
            MikeRoute.startActivity(context,destination = MikeRoute.Destination.NOTICE_LIST)
        }

        val item_favorite = view.findViewById<TextView>(R.id.item_favorite)
        item_favorite.setText(R.string.if_favorite)
        item_favorite.append(ITEM_PLACE_HOLDER + getString(R.string.item_favorite))

        val item_address = view.findViewById<TextView>(R.id.item_address)
        item_address.setText(R.string.if_address)
        item_address.append(ITEM_PLACE_HOLDER + getString(R.string.item_address))

        val item_history = view.findViewById<TextView>(R.id.item_history)
        item_history.setText(R.string.if_history)
        item_history.append(ITEM_PLACE_HOLDER + getString(R.string.item_history))

        mikeBanner = view.findViewById(R.id.mike_banner)

        queryLoginUserData()
        queryCourseNotice()


    }

    private fun queryCourseNotice() {
        ApiFactory.create(AccountApi::class.java).notice()
            .enqueue(object : MikeCallback<CourseNotice> {
                override fun onSuccess(response: MikeResponse<CourseNotice>) {
                    if (response.data != null && response.data!!.total > 0) {
                        val notification_count =
                            view!!.findViewById<TextView>(R.id.notification_count)
                        notification_count.setText(response.data!!.total.toString())
                        notification_count.visibility = View.VISIBLE
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showToast(throwable.message)
                }

            })
    }

    private fun queryLoginUserData() {
        AccountManager.getUserProfile(this, Observer { profile->
            if (profile!=null){
                updateUI(profile)
            } else{
                showToast(getString(R.string.fetch_user_profile_failed))
            }
        },false)
    }

    private fun updateUI(userProfile: UserProfile) {
        view!!.findViewById<TextView>(R.id.user_name).text =
            if (userProfile.isLogin) userProfile.userName else getString(
                R.string.profile_not_login
            )
        view!!.findViewById<TextView>(R.id.login_desc).text =
            if (userProfile.isLogin) getString(R.string.profile_login_desc_welcome_back) else getString(
                R.string.profile_not_login
            )


        if (userProfile.isLogin) {

            if (TextUtils.isEmpty(userProfile.avatar)){
                view!!.findViewById<ImageView>(R.id.user_avatar).setImageResource(R.drawable.ic_avatar_default)
            } else{
                view!!.findViewById<ImageView>(R.id.user_avatar).loadCircle(userProfile.avatar)
            }

        } else {
            view!!.findViewById<ImageView>(R.id.user_avatar)
                .setImageResource(R.drawable.ic_avatar_default)
            view!!.findViewById<TextView>(R.id.user_name).setOnClickListener {
                AccountManager.login(context, Observer { success->
                    queryLoginUserData()
                })
            }
        }

        view!!.findViewById<TextView>(R.id.tab_item_favorite).text = spannableTabItem(
            userProfile.favoriteCount, getString(
                R.string.profile_tab_item_favorite
            )
        )
        view!!.findViewById<TextView>(R.id.tab_item_history).text = spannableTabItem(
            userProfile.browseCount, getString(
                R.string.profile_tab_item_history
            )
        )
        view!!.findViewById<TextView>(R.id.tab_item_learn).text = spannableTabItem(
            userProfile.learnMinutes, getString(
                R.string.profile_tab_item_learn
            )
        )

        updateBanner(userProfile.bannerNoticeList)
    }

    private fun updateBanner(bannerNoticeList: List<Notice>?) {
        if (bannerNoticeList == null || bannerNoticeList.size == 0) return
        var models = mutableListOf<MikeBannerMo>()
        bannerNoticeList.forEach {
            var mikeBannerMo = object : MikeBannerMo() {}
            mikeBannerMo.url = it.cover
            models.add(mikeBannerMo)
        }
        mikeBanner!!.setBannerData(R.layout.layout_profile_banner_item, models)
        mikeBanner!!.setBindAdapter { viewHolder: MikeBannerAdapter.MikeBannerViewHolder?, mo: MikeBannerMo?, position: Int ->
            if (viewHolder == null || mo == null) return@setBindAdapter
            val imageView = viewHolder.findViewById<ImageView>(R.id.banner_item_imageview)
            imageView.loadCorner(mo.url, MikeDisplayUtil.dp2px(10f, resources))
        }

        mikeBanner!!.setOnBannerClickListener { viewHolder, bannerMo, position ->
            MikeRoute.startActivity4Browser(bannerNoticeList[position].url)
        }

        mikeBanner!!.visibility = View.VISIBLE

    }

    private fun spannableTabItem(topText: Int, bottomText: String): CharSequence? {

        val spanStr = topText.toString()
        var ssb = SpannableStringBuilder()
        var ssTop = SpannableString(spanStr)

        ssTop.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.color_000)), 0, ssTop.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssTop.setSpan(AbsoluteSizeSpan(18, true), 0, ssTop.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ssTop.setSpan(StyleSpan(Typeface.BOLD), 0, ssTop.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        ssb.append(ssTop)
        ssb.append(bottomText)
        return ssb
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
/*        if (requestCode==REQUEST_CODE_LOGIN_PROFILE&&resultCode== Activity.RESULT_OK&&data!=null){
            queryLoginUserData()
        }*/
    }

}