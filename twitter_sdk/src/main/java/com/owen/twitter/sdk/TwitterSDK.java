package com.owen.twitter.sdk;

import android.app.Activity;
import android.content.Intent;

import com.owen.twitter.sdk.auth.TwitterAuthClient;
import com.owen.twitter.sdk.auth.TwitterSession;
import com.owen.twitter.sdk.common.CallbackManager;
import com.owen.twitter.sdk.share.entity.ShareContent;

import androidx.annotation.NonNull;

/**
 * Twitter SDK入口类
 * <br/>Author：yunying.zhang
 * <br/>Email: yinglovezhuzhu@gmail.com
 * <br/>Date: 2019/7/17
 */
public class TwitterSDK {

    public static void login(@NonNull Activity activity, final Callback<TwitterSession> callback) {
        TwitterAuthClient authClient = new TwitterAuthClient("J4ynteRyFzTtqcYJlCASGo39I", "UutETxA6QQmSEl7pmfKS0Tv2dSxh88RzO2300Cp4pngL1tEKbh");
        authClient.authorize(activity, callback);
    }

    public static void share(@NonNull Activity activity, ShareContent shareContent, Callback callback) {
    }

    public static boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return CallbackManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
