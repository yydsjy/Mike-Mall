package com.yyds.biz_home.goodslist

import android.os.Bundle
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.mikeyyds.common.ui.component.MikeBaseActivity
import com.mikeyyds.library.util.MikeStatusBar
import com.yyds.biz_home.R

@Route(path = "/goods/list")
class GoodsListActivity : MikeBaseActivity() {
    @JvmField
    @Autowired
    var categoryTitle: String = ""

    @JvmField
    @Autowired
    var categoryId: String = ""

    @JvmField
    @Autowired
    var subcategoryId: String = ""

    private val FRAGMENT_TAG = "GOODS_LIST_FRAGMENT"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_list)
        MikeStatusBar.setStatusBar(this, true, translucent = false)

        ARouter.getInstance().inject(this)

        findViewById<TextView>(R.id.action_back).setOnClickListener {
            onBackPressed()
        }

        findViewById<TextView>(R.id.category_title).text = categoryTitle
        var fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        if (fragment == null){
            fragment = GoodsListFragment.newInstance(categoryId, subcategoryId)
        }

        val ft = supportFragmentManager.beginTransaction()
        if(!fragment.isAdded){
            ft.add(R.id.container,fragment,FRAGMENT_TAG)
        }
        ft.show(fragment).commitNowAllowingStateLoss()

/*        MikeDataBus.with<String>("stickyData").observeSticky(this,true, Observer {
            showToast(it)
        })*/


    }
}