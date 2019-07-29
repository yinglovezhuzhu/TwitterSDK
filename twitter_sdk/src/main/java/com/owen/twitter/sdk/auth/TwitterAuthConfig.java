package com.owen.twitter.sdk.auth;

import android.os.Parcel;
import android.os.Parcelable;

import com.owen.twitter.sdk.common.CallbackManagerImpl;

/**
 * Twitter认证配置
 * <br/>Author：yunying.zhang
 * <br/>Email: yunyingzhang@rastar.com
 * <br/>Date: 2019/7/23
 */
public class TwitterAuthConfig implements Parcelable {

    /**
     * The default request code to use for Single Sign On. This code will
     * be returned in {@link android.app.Activity#onActivityResult(int, int, android.content.Intent)}
     */
    public static final int DEFAULT_AUTH_REQUEST_CODE = CallbackManagerImpl.RequestCodeOffset.TwitterLogin.toRequestCode();

    public static final Creator<TwitterAuthConfig> CREATOR
            = new Creator<TwitterAuthConfig>() {
        public TwitterAuthConfig createFromParcel(Parcel in) {
            return new TwitterAuthConfig(in);
        }

        public TwitterAuthConfig[] newArray(int size) {
            return new TwitterAuthConfig[size];
        }
    };

    private final String consumerKey;
    private final String consumerSecret;

    /**
     * @param consumerKey    The consumer key.
     * @param consumerSecret The consumer secret.
     *
     * @throws IllegalArgumentException if consumer key or consumer secret is null.
     */
    public TwitterAuthConfig(String consumerKey, String consumerSecret) {
        if (consumerKey == null || consumerSecret == null) {
            throw new IllegalArgumentException(
                    "TwitterAuthConfig must not be created with null consumer key or secret.");
        }
        this.consumerKey = sanitizeAttribute(consumerKey);
        this.consumerSecret = sanitizeAttribute(consumerSecret);
    }

    private TwitterAuthConfig(Parcel in) {
        consumerKey = in.readString();
        consumerSecret = in.readString();
    }

    /**
     * @return the consumer key
     */
    public String getConsumerKey() {
        return consumerKey;
    }

    /**
     * @return the consumer secret
     */
    public String getConsumerSecret() {
        return consumerSecret;
    }

    /**
     * @return The request code to use for Single Sign On. This code will
     * be returned in {@link android.app.Activity#onActivityResult(int, int, android.content.Intent)}
     * when the activity exits.
     */
    public int getRequestCode() {
        return DEFAULT_AUTH_REQUEST_CODE;
    }

    static String sanitizeAttribute(String input) {
        if (input != null) {
            return input.trim();
        } else {
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(consumerKey);
        out.writeString(consumerSecret);
    }
}
