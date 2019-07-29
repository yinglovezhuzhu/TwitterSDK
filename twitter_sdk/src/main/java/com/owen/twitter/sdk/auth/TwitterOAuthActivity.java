package com.owen.twitter.sdk.auth;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.owen.twitter.sdk.Callback;
import com.owen.twitter.sdk.R;
import com.owen.twitter.sdk.StatusCode;
import com.owen.twitter.sdk.auth.mvp.IOAuthView;
import com.owen.twitter.sdk.auth.mvp.OAuthPresenter;
import com.owen.twitter.sdk.utils.LogUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Twitter登录页面
 * <br/>Author：yunying.zhang
 * <br/>Email: yunyingzhang@rastar.com
 * <br/>Date: 2019/5/27
 */
public class TwitterOAuthActivity extends AppCompatActivity implements View.OnClickListener, IOAuthView {

    /**
     * 数据传递key：systemUiVisibility
     */
    public static final String EXTRA_SYSTEM_UI_VISIBILITY = "extra_systemUiVisibility";

    /**
     * 数据传递Key：数据部分（String）
     */
    public static final String EXTRA_MSG = "extra_msg";

    /**
     * 数据传递Key：认证参数配置
     */
    public static final String EXTRA_AUTH_CONFIG = "extra_auth_config";

    /**
     * 请求API地址：Twitter授权认证（拼接request token后在浏览器（WebView）打开）
     */
    private static final String API_TWITTER_AUTHENTICATE = "https://api.twitter.com/oauth/authenticate";

    private WebView mWebView;
    private ProgressBar mPbLoading;

    private boolean mInLoginFlow = false;

    private TwitterAuthConfig mAuthConfig;
    private TwitterAccessToken mOAuthToken;

    private String mAuthCallbackPrefix;

    private OAuthPresenter mOAuthPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Window window = getWindow();
        if (null != window && intent.hasExtra(EXTRA_SYSTEM_UI_VISIBILITY)) {
            window.getAttributes().systemUiVisibility = intent.getIntExtra(EXTRA_SYSTEM_UI_VISIBILITY, 0);
        }

        setContentView(R.layout.activity_twitter_login);

        initView();

        mAuthConfig = intent.getParcelableExtra(EXTRA_AUTH_CONFIG);

        if(null == mAuthConfig) {
            exit(RESULT_FIRST_USER, "Twitter auth config is null", null);
            return;
        }

        mPbLoading.setVisibility(View.VISIBLE);
        mInLoginFlow = false;
        mOAuthToken = null;

        mOAuthPresenter = new OAuthPresenter(this, this);
        mAuthCallbackPrefix = mOAuthPresenter.buildCallbackUrl(mAuthConfig);

        mOAuthPresenter.getTwitterLoginRequestToken(mAuthConfig, new Callback<String>() {
            @Override
            public void onResult(int code, String msg, String data) {
                LogUtils.d(String.format("Get twitter login request token result: code = %d, msg = %s, data = %s", code, msg, data));
                mPbLoading.setVisibility(View.GONE);
                if(StatusCode.CODE_SUCCESS == code) {
                    if(TextUtils.isEmpty(data)) {
                        exit(RESULT_FIRST_USER, msg, null);
                        return;
                    }
                    // 获取request token成功
                    twitterAuthenticate(data);
                } else {
                    exit(RESULT_FIRST_USER, msg, null);
                }
            }
        });
    }

    private void initView() {
        mWebView = findViewById(R.id.rs_wv_twitter_login);
        mPbLoading = findViewById(R.id.rs_pb_twitter_login);

        setWebViewConfig(mWebView);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                if(mInLoginFlow && url.startsWith("https://api.twitter.com/oauth/authorize") && TextUtils.isEmpty(mOAuthToken)) {
//                    mInLoginFlow = false;
//                    exit(RESULT_FIRST_USER, "User deny login request.", null);
//                }
                LogUtils.d("onPageStarted--" + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtils.d("onPageFinished--" + url);
            }

            @Override
            @TargetApi(23)
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                LogUtils.e("onReceivedHttpError" + errorResponse.getStatusCode());
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                LogUtils.e("onReceivedSslError" + error.getUrl());
            }

            @Override
            @TargetApi(23)
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                final Uri url = request.getUrl();
                LogUtils.e(String.format(Locale.getDefault(),
                        "onReceivedError--%s--%s[%d]",url.toString(), error.getDescription(), error.getErrorCode()));
                if(url.toString().startsWith(API_TWITTER_AUTHENTICATE)) {
                    exit(RESULT_FIRST_USER, String.format(Locale.getDefault(),
                            "Login twitter http error.%s[%d]", error.getDescription(), error.getErrorCode()), null);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                LogUtils.e(String.format(Locale.getDefault(),
                        "onReceivedError--%s--%s[%d]", failingUrl, description, errorCode));
                if(!TextUtils.isEmpty(failingUrl) && failingUrl.startsWith(API_TWITTER_AUTHENTICATE)) {
                    exit(RESULT_FIRST_USER, String.format(Locale.getDefault(),
                            "Login twitter http error.%s[%d]", description, errorCode), null);
                }
            }

            @Override
            @TargetApi(24)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                final Uri url = request.getUrl();
                LogUtils.d(url.toString());
                final String requestUrl = url.getPath();
                handleUrlLoading(requestUrl);
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.d(url);
                if(handleUrlLoading(url)) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            private boolean handleUrlLoading(String url) {
                if(null != url && url.startsWith(mAuthCallbackPrefix)) {
//                    rsdktwitter://callback?version=xxx&app=xxx&oauth_token=qlo9pAAAAAAA89JZAAABayBeRXU&oauth_verifier=8OLt4NlForEiQ7lbL52K3IQmTH2QM0OX
                    final String urlParamsString = url.substring(url.lastIndexOf("?") + 1);
                    LogUtils.d("Twitter auth redirect result: " + urlParamsString);

                    final Map<String, String> params = parseUrlParams(urlParamsString);
                    // 校验oauth_token跟之前是否一致
                    if(params.containsKey(OAuthConstants.PARAM_TOKEN)
                            && !mOAuthToken.getToken().equals(params.get(OAuthConstants.PARAM_TOKEN))) {
                        // oauth_token跟之前传入的不一致
                        exit(RESULT_FIRST_USER, "Verify failed", null);
                        return true;
                    }
                    if(params.containsKey(OAuthConstants.PARAM_VERIFIER)) {
                        final String oauthVerify = params.get(OAuthConstants.PARAM_VERIFIER);
                        if(null != oauthVerify) {
                            mOAuthPresenter.getTwitterAccessToken(mAuthConfig, mOAuthToken, oauthVerify, new Callback<String>() {
                                @Override
                                public void onResult(int code, String msg, String data) {
                                    LogUtils.d(String.format("Twitter login result: " + data));
                                    if(StatusCode.CODE_SUCCESS == code) {
//                                        oauth_token=2888304518-Pfu5dDahdYdVshzPC0faLuQ69D6fYwEF1K9VIxE&oauth_token_secret=Hufj8bCkc5L9vfmreLQ7doVDadDWQ1nVQf6dvtdeIBbqV&user_id=2888304518&screen_name=yinglovezhuzhu
                                        Map<String, String> params = parseUrlParams(data);
                                        Bundle bundle = new Bundle();
                                        bundle.putString(AuthHandler.EXTRA_TOKEN, params.get(OAuthConstants.PARAM_TOKEN));
                                        bundle.putString(AuthHandler.EXTRA_TOKEN_SECRET, params.get("oauth_token_secret"));
                                        try {
                                            final String userId = params.get("user_id");
                                            bundle.putLong(AuthHandler.EXTRA_USER_ID, null == userId ? -1 : Long.valueOf(userId));
                                        } catch (Exception e) {
                                            // do nothing
                                        }
                                        bundle.putString(AuthHandler.EXTRA_SCREEN_NAME, params.get("screen_name"));
                                        exit(RESULT_OK, "Login Twitter success", bundle);
                                    } else {
                                        exit(RESULT_FIRST_USER, msg, null);
                                    }
                                }
                            });
                            return true;
                        }
                    }
                    // 获取oauth_verify失败了
                    exit(RESULT_FIRST_USER, "Verify user failed.", null);
                    return true;
                }
                return false;
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if(R.id.rs_ibtn_twitter_login_close == id) {
            // 关闭
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        exit(RESULT_CANCELED, "User canceled", null);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebViewConfig(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setLoadsImagesAutomatically(true);
        //调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        settings.setJavaScriptEnabled(true);

        //有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下
        //然后 复写 WebChromeClient的onCreateWindow方法
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    /**
     * 退出页面
     * @param resultCode 结果码
     * @param msg 返回的消息
     * @param data 返回的data
     */
    private void exit(final int resultCode, final String msg, final Bundle data) {
        LogUtils.d(String.format(Locale.getDefault(), "Login twitter result: \nresultCode: %d\nmsg: %s\ndata: %s", resultCode, msg, data));
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                if(!TextUtils.isEmpty(msg)) {
                    intent.putExtra(EXTRA_MSG, msg);
                }
                if(null != data) {
                    intent.putExtras(data);
                }
                setResult(resultCode, intent);
                finish();
            }
        });
    }

    /**
     * 跳转Twitter认证
     * @param requestToken request token
     */
    private void twitterAuthenticate(@NonNull final String requestToken) {
        final Map<String, String> params = parseUrlParams(requestToken);
        if(params.isEmpty() || !params.containsKey(OAuthConstants.PARAM_TOKEN)) {
            exit(RESULT_FIRST_USER, "Get request token failed", null);
            return;
        }
        mInLoginFlow = true;
        mOAuthToken = new TwitterAccessToken(params.get(OAuthConstants.PARAM_TOKEN), null);
        mWebView.loadUrl(API_TWITTER_AUTHENTICATE + "?" + OAuthConstants.PARAM_TOKEN + "=" + params.get(OAuthConstants.PARAM_TOKEN));
    }

    /**
     * 解析Url参数
     * @param params url参数（格式为key1=value1&key2=values...）
     * @return
     */
    private Map<String, String> parseUrlParams(String params) {
        final Map<String, String> paramsMap = new HashMap<>(0);
        if(!TextUtils.isEmpty(params)) {
            final String [] paramsArray = params.split("&");
            for(String p : paramsArray) {
                if(p.contains("=")) {
                    final String [] kv = p.split("=");
                    if(kv.length == 2 && !TextUtils.isEmpty(kv[0]) && !TextUtils.isEmpty(kv[1])) {
                        paramsMap.put(kv[0], kv[1]);
                    }
                }
            }
        }
        return paramsMap;
    }
}
