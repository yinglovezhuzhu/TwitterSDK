package com.owen.twitter.sdk;

/**
 * 回调
 * <br/>Author：yunying.zhang
 * <br/>Email: yinglovezhuzhu@gmail.com
 * <br/>Date: 2019/7/17
 */
public interface Callback<T> {

    void onResult(int code, String msg, T data);
}
