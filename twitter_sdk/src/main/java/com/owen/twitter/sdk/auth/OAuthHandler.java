package com.owen.twitter.sdk.auth;

import android.app.Activity;
import android.content.Intent;

import com.owen.twitter.sdk.Callback;

/**
 * <br/>Author：yunying.zhang
 * <br/>Email: yunyingzhang@rastar.com
 * <br/>Date: 2019/7/25
 */
public class OAuthHandler extends AuthHandler {

    OAuthHandler(TwitterAuthConfig authConfig, Callback<TwitterSession> callback,
                   int requestCode) {
        super(authConfig, callback, requestCode);
    }

    @Override
    public boolean authorize(Activity activity) {
        try {
            Intent intent = new Intent(activity, TwitterOAuthActivity.class);
            int systemUiVisibility = activity.getWindow().getAttributes().systemUiVisibility;
            intent.putExtra(TwitterOAuthActivity.EXTRA_SYSTEM_UI_VISIBILITY, systemUiVisibility);
            intent.putExtra(TwitterOAuthActivity.EXTRA_AUTH_CONFIG, mAuthConfig);
            activity.startActivityForResult(intent, requestCode);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
