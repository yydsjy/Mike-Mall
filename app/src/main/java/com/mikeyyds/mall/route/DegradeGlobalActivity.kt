package com.mikeyyds.mall.route

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.mikeyyds.common.ui.view.EmptyView
import com.mikeyyds.mall.R
import com.mikeyyds.ui.icfont.IconFontTextView

@Route(path = "/degrade/global/activity")
class DegradeGlobalActivity: AppCompatActivity() {
    @JvmField
    @Autowired
    var degrade_title:String? = null
    @JvmField
    @Autowired
    var degrade_desc:String? = null
    @JvmField
    @Autowired
    var degrade_action: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)

        setContentView(R.layout.layout_global_degrade)

        var emptyView = findViewById<EmptyView>(R.id.empty_view)
        emptyView.setIcon(R.string.if_degrade)
        if (degrade_title!=null) {
            emptyView.setTitle(degrade_title!!)
        }

        if (degrade_desc!=null){
            emptyView.setDesc(degrade_desc!!)
        }

        if (degrade_action!=null){
            emptyView.setHelpAction(listener = object:View.OnClickListener{
                override fun onClick(v: View?) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(degrade_action))
                    startActivity(intent)
                }
            })
        }

        findViewById<IconFontTextView>(R.id.action_back).setOnClickListener{onBackPressed()}

    }

}