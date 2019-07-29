package com.owen.twitter.sdk.auth;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Twitter登录token
 * <br/>Author：yunying.zhang
 * <br/>Email: yunyingzhang@rastar.com
 * <br/>Date: 2019/7/23
 */
public class TwitterAccessToken implements Parcelable {

    @SerializedName("token")
    private final String token;

    @SerializedName("secret")
    private final String secret;

    public TwitterAccessToken(String token, String secret) {
        this.token = token;
        this.secret = secret;
    }

    private TwitterAccessToken(Parcel in) {
        token = in.readString();
        secret = in.readString();
    }

    public static final Creator<TwitterAccessToken> CREATOR = new Creator<TwitterAccessToken>() {
        @Override
        public TwitterAccessToken createFromParcel(Parcel in) {
            return new TwitterAccessToken(in);
        }

        @Override
        public TwitterAccessToken[] newArray(int size) {
            return new TwitterAccessToken[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeString(secret);
    }

    public String getToken() {
        return token;
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public String toString() {
        return "TwitterAccessToken{" +
                "token='" + token + '\'' +
                ", secret='" + secret + '\'' +
                '}';
    }
}
