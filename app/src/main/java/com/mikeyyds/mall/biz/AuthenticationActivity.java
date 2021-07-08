package com.mikeyyds.mall.biz;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.mikeyyds.mall.route.RouteFlag;

@Route(path = "/profile/authentication", extras = RouteFlag.FLAG_AUTHENTICATION)
public class AuthenticationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
