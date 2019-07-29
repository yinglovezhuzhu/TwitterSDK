package com.owen.twitter.sdk.common;

import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

/**
 * onActivityResult回调管理类实现<br>
 * Created by yinglovezhuzhu@gmail.com on 2015/6/5.
 */
public final class CallbackManagerImpl implements ICallbackManager {

    private static int mCallbackRequestCodeOffset = 0xFACE + 100;

    private static Map<Integer, Callback> mStaticCallbacks = new HashMap<>();

    /**
     * If there is no explicit callback, but we still need to call the Facebook component,
     * because it's going to update some state, e.g., login, like. Then we should register a
     * static callback that can still handle the response.
     * @param requestCode The request code.
     * @param callback The callback for the feature.
     */
    public synchronized static void registerStaticCallback(
            int requestCode,
            Callback callback) {
        if (callback == null) {
            throw new NullPointerException("Argument callback cannot be null");
        }
        if (mStaticCallbacks.containsKey(requestCode)) {
            return;
        }
        mStaticCallbacks.put(requestCode, callback);
    }

    private static synchronized Callback getStaticCallback(Integer requestCode) {
        return mStaticCallbacks.get(requestCode);
    }

    private static boolean runStaticCallback(
            int requestCode,
            int resultCode,
            Intent data) {
        Callback callback = getStaticCallback(requestCode);
        if (callback != null) {
            return callback.onActivityResult(resultCode, data);
        }
        return false;
    }

    private Map<Integer, Callback> callbacks = new HashMap<>();

    public void registerCallback(int requestCode, Callback callback) {
        if (callback == null) {
            throw new NullPointerException("Argument callback cannot be null");
        }
        callbacks.put(requestCode, callback);
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        Callback callback = callbacks.get(requestCode);
        if (callback != null) {
            return callback.onActivityResult(resultCode, data);
        }
        return runStaticCallback(requestCode, resultCode, data);
    }

    /**
     * 请求码枚举定义
     */
    public static enum RequestCodeOffset {
        FBLogin(0),
        FBShare(1),
        GGLogin(2),
        GGIabBilling(3),
        RequestPermission(4)/* 运行时授权 */,
        SettingPermission(5)/* 去应用详情中配置权限 */,
        PICK_FILE(6),
        RSLoginDialog(7) /* RS登录窗口 */,
        RSFloatWindow(8) /* RS悬浮侧边栏 */,
        LineShare(9)/* Line 分享 */,
        ONEStorePay(10)/* One Store支付 */,
        ONEStoreLogin(11)/* One Store登录 */,
        RSServiceCenter(12)/* RastarServiceCenter */,
        TwitterLogin(13)/* RastarServiceCenter */;

        private final int offset;

        private RequestCodeOffset(int offset) {
            this.offset = offset;
        }

        public int toRequestCode() {
            return mCallbackRequestCodeOffset + this.offset;
        }
    }

    public interface Callback {
        public boolean onActivityResult(int resultCode, Intent data);
    }
}
