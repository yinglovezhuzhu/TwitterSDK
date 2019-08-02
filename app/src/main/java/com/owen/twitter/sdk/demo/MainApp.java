package com.owen.twitter.sdk.demo;

import android.app.Application;

import com.owen.twitter.sdk.TwitterSDK;

/**
 * <br/>Author：yunying.zhang
 * <br/>Email: yunyingzhang@rastar.com
 * <br/>Date: 2019/7/19
 */
public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TwitterSDK.init(this);
    }
}
