package com.owen.twitter.sdk.auth;

import android.app.Activity;

import com.owen.twitter.sdk.Callback;

/**
 * Abstract class for handling authorization requests.
 */
public abstract class AuthHandler {
    static final String EXTRA_TOKEN = "tk";
    static final String EXTRA_TOKEN_SECRET = "ts";
    static final String EXTRA_SCREEN_NAME = "screen_name";
    static final String EXTRA_USER_ID = "user_id";
    static final String EXTRA_AUTH_ERROR = "auth_error";

    static final int RESULT_CODE_ERROR = Activity.RESULT_FIRST_USER;

    protected final int requestCode;
    protected TwitterAuthConfig mAuthConfig;
    private final Callback<TwitterSession> callback;

    /**
     * @param authConfig  The {@link TwitterAuthConfig}.
     * @param callback    The listener to callback when authorization completes.
     * @param requestCode The request code.
     */
    AuthHandler(TwitterAuthConfig authConfig, Callback<TwitterSession> callback, int requestCode) {
        mAuthConfig = authConfig;
        this.callback = callback;
        this.requestCode = requestCode;
    }

    TwitterAuthConfig getAuthConfig() {
        return mAuthConfig;
    }

    Callback<TwitterSession> getCallback() {
        return callback;
    }

    /**
     * Called to request authorization.
     *
     * @return true if authorize request was successfully started.
     */
    public abstract boolean authorize(Activity activity);
}
