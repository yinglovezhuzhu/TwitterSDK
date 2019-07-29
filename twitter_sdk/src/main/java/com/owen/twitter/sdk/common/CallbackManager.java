package com.owen.twitter.sdk.common;

import android.content.Intent;

/**
 * onActivityResult回调管理
 * Created by yinglovezhuzhu@gmail.com on 2016/8/26.
 */
public class CallbackManager {

    private final ICallbackManager mCallbackManager = ICallbackManager.Factory.create();

    private static CallbackManager mInstance = null;

    private CallbackManager() {

    }

    public static CallbackManager getInstance() {
        synchronized (CallbackManager.class) {
            if(null == mInstance) {
                mInstance = new CallbackManager();
            }
            return mInstance;
        }
    }

    /**
     * 注册回调，处理onActivityResult回调
     * @param requestCode 请求码
     * @param callback 回调
     */
    public void registerCallbackImpl(final int requestCode,
                                      final CallbackManagerImpl.Callback callback) {
        if (!(mCallbackManager instanceof CallbackManagerImpl)) {
            throw new IllegalStateException("Unexpected CallbackManager, " +
                    "please use the provided Factory.");
        }

        ((CallbackManagerImpl) mCallbackManager).registerCallback(requestCode, callback);

    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
