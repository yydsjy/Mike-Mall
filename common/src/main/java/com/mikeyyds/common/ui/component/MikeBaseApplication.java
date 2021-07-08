package com.mikeyyds.common.ui.component;

import android.app.Application;

import com.google.gson.Gson;
import com.mikeyyds.library.log.MikeConsolePrinter;
import com.mikeyyds.library.log.MikeFilePrinter;
import com.mikeyyds.library.log.MikeLogConfig;
import com.mikeyyds.library.log.MikeLogManager;

public class MikeBaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initLog();
    }

    private void initLog() {
        MikeLogManager.init(new MikeLogConfig() {
            @Override
            public JsonParser injectParser() {
                return (src -> new Gson().toJson(src));
            }

            @Override
            public boolean includeThread() {
                return true;
            }
        }, new MikeConsolePrinter(), MikeFilePrinter.getInstance(getCacheDir().getAbsolutePath(), 0));
    }
}
