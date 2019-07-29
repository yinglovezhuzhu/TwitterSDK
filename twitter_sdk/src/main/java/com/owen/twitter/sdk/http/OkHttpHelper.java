package com.owen.twitter.sdk.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.owen.twitter.sdk.StatusCode;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OkHttp帮助类
 * <br/>Author：yunying.zhang
 * <br/>Email: yunyingzhang@rastar.com
 * <br/>Date: 2019/5/27
 */
public class OkHttpHelper {

    private OkHttpClient mOkHttpClient;

    private static OkHttpHelper mInstance = null;

    private OkHttpHelper() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS);
        mOkHttpClient = builder.build();
    }

    public static OkHttpHelper getInstance() {
        synchronized (OkHttpHelper.class) {
            if(null == mInstance) {
                mInstance = new OkHttpHelper();
            }
            return mInstance;
        }
    }

    /**
     * Post请求
     * @param url 请求地址
     * @param tag Tag标签，可为空
     * @param headers 头部，可为空
     * @param params body参数，可为空
     * @param callback 回调，可为空
     */
    public void doPostWithMultiPart(@NonNull final String url, @Nullable final Object tag,
                                    @Nullable final Map<String, String> headers,
                                    @Nullable final Map<String, String> params,
                                    @Nullable final com.owen.twitter.sdk.Callback<String> callback) {

        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("Request url can not be empty!");
        }

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if(null != tag) {
            requestBuilder.tag(tag);
        }

        // 添加Headers
        if(null != headers && !headers.isEmpty()) {
            for(Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        // 添加Form data
        if (params != null && !params.isEmpty()) {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                multipartBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        } else {
            // 保证part不为空
            multipartBodyBuilder.addPart(new FormBody.Builder().build());
        }

        requestBuilder.post(multipartBodyBuilder.build());
        final Request request = requestBuilder.build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                if(null != callback) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResult(StatusCode.CODE_FAILED, e.getMessage(), null);
                        }
                    });
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) {
                if (null != callback) {
                    final ResponseBody responseBody = response.body();
                    String bodyStr = "";
                    if (null != responseBody) {
                        try {
                            bodyStr = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    final String responseData = bodyStr;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (200 == response.code()) {
                                callback.onResult(StatusCode.CODE_SUCCESS,
                                        TextUtils.isEmpty(response.message()) ? "Success" : response.message(),
                                        responseData);
                            } else {
                                callback.onResult(StatusCode.CODE_FAILED,
                                        String.format(Locale.getDefault(), "%s[%d]",
                                                TextUtils.isEmpty(response.message()) ? "Failed" : response.message(), response.code()),
                                        responseData);
                            }
                        }
                    });
                }
            }
        });
    }


    /**
     * 取消请求
     * @param tag tag
     */
    public void cancelRequest(Object tag) {
        Dispatcher dispatcher = mOkHttpClient.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
}
