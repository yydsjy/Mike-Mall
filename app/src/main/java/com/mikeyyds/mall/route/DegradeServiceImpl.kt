package com.mikeyyds.mall.route

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.DegradeService
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = "/degrade/global/service")
class DegradeServiceImpl: DegradeService {
    override fun onLost(context: Context?, postcard: Postcard?) {
        ARouter.getInstance().build("/degrade/global/activity").greenChannel().navigation()
    }

    override fun init(context: Context?) {
    }
}