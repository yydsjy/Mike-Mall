package com.yyds.biz_home.home

import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.mikeyyds.common.route.MikeRoute
import com.mikeyyds.common.ui.component.MikeBaseFragment
import com.mikeyyds.library.util.MikeDisplayUtil
import com.mikeyyds.ui.icfont.IconFontTextView
import com.mikeyyds.ui.search.MikeSearchView

import com.mikeyyds.ui.tab.bottom.MikeTabBottomLayout
import com.mikeyyds.ui.tab.common.IMikeTabLayout
import com.mikeyyds.ui.tab.top.MikeTabTopInfo
import com.mikeyyds.ui.tab.top.MikeTabTopLayout
import com.mikeyyds.ui.title.MikeNavigationBar
import com.yyds.biz_home.R
import com.yyds.biz_home.model.TabCategory

class HomePageFragment : MikeBaseFragment() {
    private var selectedTabIndex: Int = 0
    private val DEFAULT_SELECTED_TAB_INDEX: Int = 0
    private val topTabs = mutableListOf<MikeTabTopInfo<Int>>()

    private val onTabSelectedListener =
        IMikeTabLayout.OnTabSelectedListener<MikeTabTopInfo<*>> { index, prevInfo, nextInfo ->
            val viewPager = layoutView.findViewById<ViewPager>(R.id.view_pager)
            if (viewPager.currentItem != index) {
                viewPager.setCurrentItem(index, false)
            }
        }

    override fun getLayouId(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = layoutView.findViewById<ViewPager>(R.id.view_pager)
        MikeTabBottomLayout.clipBottomPadding(viewPager)
        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.queryCategoryTabs().observe(viewLifecycleOwner, Observer {
            it?.let {
                updateUI(it)
            }
        })

/*        val navigationBar = view.findViewById<MikeNavigationBar>(R.id.navigation_bar)
        navigationBar.setNavListener(View.OnClickListener { activity?.finish() })
        navigationBar.addRightTextButton("Search",View.generateViewId())

        val searchView = MikeSearchView(context!!)
        searchView.layoutParams = ViewGroup.LayoutParams(-1,MikeDisplayUtil.dp2px(40f))
        searchView.postDelayed({
            searchView.setKeyWord("mike",View.OnClickListener {})
        },2000)
        searchView.setHintText("Search")
        searchView.setClearIconClickListener(View.OnClickListener {  })
        navigationBar.addCenterView(searchView)*/

        view.findViewById<IconFontTextView>(R.id.search_container)
            .setOnClickListener { MikeRoute.startActivity(context, null, MikeRoute.Destination.SEARCH_MAIN) }

    }


    private fun updateUI(data: List<TabCategory>) {
        if (!isAlive) return
        topTabs.clear()

        data.forEachIndexed { index, tabCategory ->
            val defaultColor = ContextCompat.getColor(context!!, R.color.color_333)
            val selectColor = ContextCompat.getColor(context!!, R.color.color_DD2)
            val tabTopInfo =
                MikeTabTopInfo<Int>(tabCategory.categoryName, defaultColor, selectColor)

            topTabs.add(tabTopInfo)
        }
        val viewPager = layoutView.findViewById<ViewPager>(R.id.view_pager)
        val toptabLayout = layoutView.findViewById<MikeTabTopLayout>(R.id.top_tab_layout)
        toptabLayout.inflateInfo(topTabs as List<MikeTabTopInfo<*>>)
        toptabLayout.defaultSelected(topTabs[DEFAULT_SELECTED_TAB_INDEX])
        toptabLayout.addTabSelectedChangeListener(onTabSelectedListener)

        if (viewPager.adapter == null) {
            viewPager.adapter = HomePagerAdapter(
                childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            )

            viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    if (position != selectedTabIndex)
                        toptabLayout.defaultSelected(topTabs[position])
                    selectedTabIndex = position
                }
            })
        }

        (viewPager.adapter as HomePagerAdapter).update(data)
    }

    inner class HomePagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(fm, behavior) {
        val data = mutableListOf<TabCategory>()
        val fragments = SparseArray<Fragment>(data.size)
        override fun getItem(position: Int): Fragment {
            val categoryId = data[position].categoryId
            var fragment = fragments.get(categoryId.toInt(), null)
            if (fragment == null) {
                fragment = HomeTabFragment.newInstance(data[position].categoryId)
                fragments.put(categoryId.toInt(), fragment)
            }
            return fragment
        }

        override fun getCount(): Int {
            return data.size
        }

        override fun getItemPosition(`object`: Any): Int {
            val indexOfValue = fragments.indexOfValue(`object` as Fragment)
            val fragment = getItem(indexOfValue)
            return if (fragment == `object`) PagerAdapter.POSITION_UNCHANGED else
                PagerAdapter.POSITION_NONE
        }

        override fun getItemId(position: Int): Long {
            return data[position].categoryId.toLong()
        }

        fun update(list: List<TabCategory>) {
            data.clear()
            data.addAll(list)
            notifyDataSetChanged()
        }

    }

}