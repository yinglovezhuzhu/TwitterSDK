package com.owen.twitter.sdk.share.entity;

import android.net.Uri;

/**
 * 分享适配内容容器
 * <br/>Author：yunying.zhang
 * <br/>Email: yinglovezhuzhu@gmail.com
 * <br/>Date: 2019/7/17
 */
public class ShareVideoContent extends ShareContent<ShareVideoContent, ShareVideoContent.Builder> {

    private final String contentDescription;
    private final String contentTitle;
    private final Uri videoUri;

    protected ShareVideoContent(Builder builder) {
        super(builder);
        this.contentTitle = builder.contentTitle;
        this.contentDescription = builder.contentDescription;
        this.videoUri = builder.photoUri;
    }

    /**
     * 获取描述
     * @return 描述内容
     */
    public String getContentDescription() {
        return contentDescription;
    }

    /**
     * 获取标题
     * @return 标题
     */
    public String getContentTitle() {
        return contentTitle;
    }

    /**
     * 获取分享视频Uri
     * @return 分享视频Uri
     */
    public Uri getVideoUri() {
        return videoUri;
    }

    /**
     * 分享图片内容构建器
     */
    public static class Builder extends ShareContent.Builder<ShareVideoContent, Builder> {
        private String contentDescription;
        private String contentTitle;
        private Uri photoUri;

        /**
         * 设置内容描述（可选）
         * @param contentDescription 内容标题
         * @return
         */
        public Builder setContentDescription(String contentDescription) {
            this.contentDescription = contentDescription;
            return this;
        }

        /**
         * 设置内容标题（可选）
         * @param contentTitle 内容标题
         * @return
         */
        public Builder setContentTitle(String contentTitle) {
            this.contentTitle = contentTitle;
            return this;
        }

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
        public ShareVideoContent build() {
            return new ShareVideoContent(this);
        }
    }
}
