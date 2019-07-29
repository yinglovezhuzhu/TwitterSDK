package com.owen.twitter.sdk.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.owen.twitter.sdk.Callback;
import com.owen.twitter.sdk.StatusCode;
import com.owen.twitter.sdk.common.CallbackManager;
import com.owen.twitter.sdk.common.CallbackManagerImpl;
import com.owen.twitter.sdk.utils.LogUtils;

/**
 * Twitter认证客户端
 * <br/>Author：yunying.zhang
 * <br/>Email: yunyingzhang@rastar.com
 * <br/>Date: 2019/7/23
 */
public class TwitterAuthClient {

    private TwitterAuthConfig mAuthConfig;


    public TwitterAuthClient(String apiKey, String apiSecret) {
        mAuthConfig = new TwitterAuthConfig(apiKey, apiSecret);
    }


    public int getRequestCode() {
        return mAuthConfig.getRequestCode();
    }

    /**
     * 认证
     * @param activity
     * @param callback
     */
    public void authorize(Activity activity, Callback<TwitterSession> callback) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity must not be null.");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Callback must not be null.");
        }

        if (activity.isFinishing()) {
            LogUtils.e("Cannot authorize, activity is finishing.", null);
        } else {
            handleAuthorize(activity, callback);
        }
    }

    private void handleAuthorize(Activity activity, final Callback<TwitterSession> callback) {
        CallbackManager.getInstance().registerCallbackImpl(getRequestCode(), new CallbackManagerImpl.Callback() {
            @Override
            public boolean onActivityResult(int resultCode, Intent data) {
                if (null != callback) {
                    if (Activity.RESULT_CANCELED == resultCode) {
                        callback.onResult(StatusCode.CODE_CANCELED, "Login Twitter user canceled", null);
                    } else if (Activity.RESULT_OK == resultCode) {
                        final Bundle bundle = data.getExtras();
                        if(null == bundle) {
                            callback.onResult(StatusCode.CODE_FAILED, "Login Twitter user failed", null);
                            return true;
                        }
//                        oauth_token=2888304518-Pfu5dDahdYdVshzPC0faLuQ69D6fYwEF1K9VIxE&oauth_token_secret=Hufj8bCkc5L9vfmreLQ7doVDadDWQ1nVQf6dvtdeIBbqV&user_id=2888304518&screen_name=yinglovezhuzhu
                        TwitterAccessToken accessToken = new TwitterAccessToken(bundle.getString(AuthHandler.EXTRA_TOKEN),
                                bundle.getString(AuthHandler.EXTRA_TOKEN_SECRET));
                        TwitterSession twitterSession = new TwitterSession(accessToken,
                                bundle.getLong(AuthHandler.EXTRA_USER_ID, -1), bundle.getString(AuthHandler.EXTRA_SCREEN_NAME));
                        callback.onResult(StatusCode.CODE_SUCCESS, "Login Twitter success", twitterSession);
                    } else {
                        callback.onResult(StatusCode.CODE_FAILED, "Login Twitter user failed", null);
                    }
                }
                return true;
            }
        });
        if (!authorizeUsingSSO(activity, callback)
                && !authorizeUsingOAuth(activity, callback)) {
            if(null != callback) {
                callback.onResult(StatusCode.CODE_FAILED, "Login Twitter failed", null);
            }
        }
    }

    /**
     * 采用SSO方式认证（调用Twitter客户端）
     * @param activity
     * @param callback
     * @return
     */
    private boolean authorizeUsingSSO(Activity activity, final Callback<TwitterSession> callback) {
        if (SSOAuthHandler.isAvailable(activity)) {
            LogUtils.d("Using SSO");
            SSOAuthHandler ssoAuthHandler = new SSOAuthHandler(mAuthConfig, callback, getRequestCode());
            return ssoAuthHandler.authorize(activity);
        }
        return false;
    }

    /**
     * 使用OAuth 1.0进行认证
     * @param activity
     * @param callback
     * @return
     */
    private boolean authorizeUsingOAuth(Activity activity, final Callback<TwitterSession> callback) {
        OAuthHandler oAuthHandler = new OAuthHandler(mAuthConfig, callback, getRequestCode());
        return oAuthHandler.authorize(activity);
    }

}
