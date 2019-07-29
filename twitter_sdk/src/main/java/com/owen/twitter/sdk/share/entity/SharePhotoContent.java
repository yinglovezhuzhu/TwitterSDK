package com.owen.twitter.sdk.share.entity;

import android.net.Uri;

/**
 * 分享图片内容容器
 * <br/>Author：yunying.zhang
 * <br/>Email: yinglovezhuzhu@gmail.com
 * <br/>Date: 2019/7/17
 */
public class SharePhotoContent extends ShareContent<SharePhotoContent, SharePhotoContent.Builder> {

    private final Uri photoUri;

    protected SharePhotoContent(Builder builder) {
        super(builder);
        this.photoUri = builder.photoUri;
    }

    /**
     * 获取分享图片Uri
     * @return 分享图片Uri
     */
    public Uri getPhotoUri() {
        return photoUri;
    }

    /**
     * 分享图片内容构建器
     */
    public static class Builder extends ShareContent.Builder<SharePhotoContent, Builder> {
        private Uri photoUri;

        /**
         * 设置分享图片Uri（必须）
         * @param photoUri 图片Uri
         * @return
         */
        public Builder setPhotoUri(Uri photoUri) {
            this.photoUri = photoUri;
            return this;
        }

        @Override
        public SharePhotoContent build() {
            return new SharePhotoContent(this);
        }
    }
}
