package com.mikeyyds.mall;

import com.alibaba.android.arouter.BuildConfig;
import com.alibaba.android.arouter.launcher.ARouter;
import com.mikeyyds.common.ui.component.MikeBaseApplication;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MikeApplication extends MikeBaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }
}
