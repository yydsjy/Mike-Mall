package com.mikeyyds.mall.route

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.mikeyyds.common.ui.view.EmptyView
import com.mikeyyds.mall.R

@Route(path = "/degrade/global/activity")
class DegradeGlobalActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.layout_global_degrage)

        var emptyView = findViewById<EmptyView>(R.id.empty_view)
        emptyView.setIcon(R.string.if_degrade)
        emptyView.setTitle("Page is missing. Programmer is finding it...")

    }

}