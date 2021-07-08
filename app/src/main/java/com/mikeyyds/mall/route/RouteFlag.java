package com.mikeyyds.mall.route;

public interface RouteFlag {
    int FLAG_LOGIN = 0x01;
    int FLAG_AUTHENTICATION = FLAG_LOGIN << 1;
    int FLAG_VIP = FLAG_AUTHENTICATION << 1;
}
