package com.owen.twitter.sdk.core;

import android.content.Context;
import android.text.TextUtils;

import com.owen.twitter.sdk.auth.TwitterAuthConfig;
import com.owen.twitter.sdk.utils.ResourcesUtils;

import androidx.annotation.NonNull;

/**
 * <br/>Author：yunying.zhang
 * <br/>Email: yunyingzhang@rastar.com
 * <br/>Date: 2019/7/24
 */
public class TwitterCore {

    private TwitterAuthConfig mAuthConfig;

    private static TwitterCore mInstance = null;

    private TwitterCore() {

    }

    public static TwitterCore getInstance() {
        synchronized (TwitterCore.class) {
            if(null == mInstance) {
                mInstance = new TwitterCore();
            }
            return mInstance;
        }
    }

    public void init(@NonNull Context context) {
        final String apiKey = ResourcesUtils.getString("twitter_api_key", context);
        final String apiSecretKey = ResourcesUtils.getString("twitter_api_secret_key", context);
        if(TextUtils.isEmpty(apiKey) || TextUtils.isEmpty(apiSecretKey)) {
            throw new IllegalStateException("Twitter api key and api secret key not found");
        }

        mAuthConfig = new TwitterAuthConfig(apiKey, apiSecretKey);
    }

}
