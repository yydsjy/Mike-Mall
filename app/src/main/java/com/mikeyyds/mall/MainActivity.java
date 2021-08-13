package com.mikeyyds.mall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.mikeyyds.common.ui.component.MikeBaseActivity;

import com.mikeyyds.library.util.MikeDataBus;
import com.mikeyyds.library.util.MikeStatusBar;
import com.mikeyyds.mall.logic.MainActivityLogic;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import kotlin.reflect.KVariance;


public class MainActivity extends MikeBaseActivity implements MainActivityLogic.ActivityProvider {
    private MainActivityLogic activityLogic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityLogic = new MainActivityLogic(this,savedInstanceState);
        MikeStatusBar.INSTANCE.setStatusBar(this,true, Color.WHITE,false);
//        MikeDataBus.INSTANCE.<String>with("stickyData").setStickyData("stickyData from Main");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            if (BuildConfig.DEBUG){
                try {
                    Class<?> aClass = Class.forName("com.yyds.debug.DebugToolDialogFragment");
                    DialogFragment target = (DialogFragment) aClass.getConstructor().newInstance();
                    target.show(getSupportFragmentManager(),"debug_tool");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment:fragments){
            fragment.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        activityLogic.onSaveInstanceState(outState);
    }
}
