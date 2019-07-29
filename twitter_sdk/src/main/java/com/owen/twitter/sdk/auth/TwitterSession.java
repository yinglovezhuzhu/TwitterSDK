package com.owen.twitter.sdk.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Twitter登录Session
 * <br/>Author：yunying.zhang
 * <br/>Email: yunyingzhang@rastar.com
 * <br/>Date: 2019/7/23
 */
public class TwitterSession {

    @SerializedName("user_id")
    private long userId;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("access_token")
    private TwitterAccessToken accessToken;

    public TwitterSession(TwitterAccessToken accessToken, long userId, String userName) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public TwitterAccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public String toString() {
        return "TwitterSession{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", accessToken=" + accessToken +
                '}';
    }
}
