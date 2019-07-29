package com.owen.twitter.sdk.common;

import android.content.Intent;

/**
 * onActivityResult回调管理类接口<br>
 * Created by yinglovezhuzhu@gmail.com on 2015/6/5.
 */
public interface ICallbackManager {

    /**
     * The method that should be called from the Activity's or Fragment's onActivityResult method.
     * @param requestCode The request code that's received by the Activity or Fragment.
     * @param resultCode  The result code that's received by the Activity or Fragment.
     * @param data        The result data that's received by the Activity or Fragment.
     * @return true If the result could be handled.
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * The factory class for the {@link ICallbackManager}.
     */
    public static class Factory {
        /**
         * Creates an instance of {@link ICallbackManager}.
         * @return an instance of {@link ICallbackManager}.
         */
        public static ICallbackManager create() {
            return new CallbackManagerImpl();
        }
    }
}
