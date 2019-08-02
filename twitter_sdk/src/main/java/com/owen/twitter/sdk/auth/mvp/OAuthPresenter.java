package com.owen.twitter.sdk.auth.mvp;

import android.content.Context;
import android.net.Uri;

import com.owen.twitter.sdk.Callback;
import com.owen.twitter.sdk.SDKVersion;
import com.owen.twitter.sdk.auth.OAuth1aHeaders;
import com.owen.twitter.sdk.auth.OAuthConstants;
import com.owen.twitter.sdk.auth.TwitterAccessToken;
import com.owen.twitter.sdk.auth.TwitterAuthConfig;
import com.owen.twitter.sdk.http.OkHttpHelper;
import com.owen.twitter.sdk.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * <br/>Author：yunying.zhang
 * <br/>Email: yunyingzhang@rastar.com
 * <br/>Date: 2019/7/26
 */
public class OAuthPresenter {

    private static final String CALLBACK_URL = "twittersdk://callback";
    /**
     * 请求API地址：Twitter request token
     */
    private static final String API_TWITTER_REQUEST_TOKEN = "https://api.twitter.com/oauth/request_token";

    /**
     * 请求API地址：Twitter获取AccessToken
     */
    private static final String API_TWITTER_ACCESS_TOKEN = "https://api.twitter.com/oauth/access_token";


    private Context mContext;
    private IOAuthView mView;


    public OAuthPresenter(Context context, IOAuthView view) {
        this.mContext = context;
        this.mView = view;
    }


    /**
     * 获取Twitter登录request token
     * @param authConfig 登录参数配置
     * @param callback 回调
     */
    public void getTwitterLoginRequestToken(@NonNull TwitterAuthConfig authConfig,
                                            @NonNull final Callback<String> callback) {
        OAuth1aHeaders oAuth1aHeaders = new OAuth1aHeaders();
        // https://api.twitter.com/oauth/request_token接口oauth_token和oauth_token_secret传空即可
        final String authorization = oAuth1aHeaders.getAuthorizationHeader(authConfig, null,
                buildCallbackUrl(authConfig), "POST", API_TWITTER_REQUEST_TOKEN, null);
        LogUtils.d("Twitter authorization: " + authorization);

        final Map<String, String> headers = new HashMap<>();
        headers.put(OAuthConstants.HEADER_AUTHORIZATION, authorization);

        OkHttpHelper.getInstance().doPostWithMultiPart(API_TWITTER_REQUEST_TOKEN, null, headers, null, callback);
    }

    /**
     * 获取Twitter access token
     * @param authConfig Twitter 认证配置（api key和api secret key）
     * @param authToken Twitter 认证token（token和token secret）
     * @param callback 回调
     */
    public void getTwitterAccessToken(@NonNull TwitterAuthConfig authConfig, @NonNull TwitterAccessToken authToken,
                                       @NonNull String verify, @NonNull final Callback<String> callback) {
        OAuth1aHeaders oAuth1aHeaders = new OAuth1aHeaders();
        final String authorization = oAuth1aHeaders.getAuthorizationHeader(authConfig, authToken,
                buildCallbackUrl(authConfig), "POST", API_TWITTER_REQUEST_TOKEN, null);
        LogUtils.d("Twitter authorization: " + authorization);

        final Map<String, String> headers = new HashMap<>();
        LogUtils.d("Twitter authorization: " + authorization);
        headers.put(OAuthConstants.HEADER_AUTHORIZATION, authorization);

        final Map<String, String> params = new HashMap<>();
        params.put(OAuthConstants.PARAM_VERIFIER, verify);

        OkHttpHelper.getInstance().doPostWithMultiPart(API_TWITTER_ACCESS_TOKEN, null, headers, params, callback);
    }


    /**
     * Builds a callback url that is used to receive a request containing the oauth_token and
     * oauth_verifier parameters.
     *
     * @param authConfig The auth config
     * @return the callback url
     */
    public String buildCallbackUrl(TwitterAuthConfig authConfig) {
        return Uri.parse(CALLBACK_URL).buildUpon()
                .appendQueryParameter("version", SDKVersion.SDK_VERSION)
                .appendQueryParameter("app", authConfig.getConsumerKey())
                .build()
                .toString();
    }

}
