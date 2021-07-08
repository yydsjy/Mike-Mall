package com.mikeyyds.mall.fragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.mikeyyds.common.ui.component.MikeBaseFragment;
import com.mikeyyds.library.executor.MikeExecutor;
import com.mikeyyds.mall.R;

public class HomePageFragment extends MikeBaseFragment {

    private String TAG = "MainActivity";
    boolean paused = false;

    @Override
    public int getLayouId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutView.findViewById(R.id.detail).setOnClickListener(v ->{
            for (int priority = 0; priority < 10; priority++) {
                int finalPriority = priority;
                MikeExecutor.INSTANCE.execute(priority,()->{
                    try {
                        Thread.sleep(1000-finalPriority*100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        );


        layoutView.findViewById(R.id.vip).setOnClickListener(v ->{
                    if (paused) {
                        MikeExecutor.INSTANCE.resume();
                    } else {
                        MikeExecutor.INSTANCE.pause();
                    }
                    paused = !paused;
                }
                );
        layoutView.findViewById(R.id.authentication).setOnClickListener(v ->MikeExecutor.INSTANCE.execute(new MikeExecutor.Callable<String>() {
                    @Override
                    public String onBackground() {
                        Log.e(TAG, "onBackground: "+Thread.currentThread().getName() );
                        return "Mike";
                    }

                    @Override
                    public void onCompleted(String s) {
                        Log.e(TAG, "onCompleted: "+Thread.currentThread().getName() );
                        Log.e(TAG, "onCompleted: "+s );
                    }
                })
                );
        layoutView.findViewById(R.id.unknown).setOnClickListener(v ->
                navigation("/profile/unknown"));
    }

    void navigation(String path){
        ARouter.getInstance().build(path).navigation();
    }
}
