package com.mikeyyds.mall;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mikeyyds.common.ui.component.MikeBaseActivity;

import com.mikeyyds.mall.biz.LoginActivity;




public class MainActivity extends MikeBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, LoginActivity.class));
    }
}
