package com.owen.twitter.sdk.share.entity;

import android.net.Uri;

/**
 * 分享链接内容容器
 * <br/>Author：yunying.zhang
 * <br/>Email: yinglovezhuzhu@gmail.com
 * <br/>Date: 2019/7/17
 */
public class ShareLinkContent extends ShareContent<ShareLinkContent, ShareLinkContent.Builder> {

    private final Uri linkUri;

    protected ShareLinkContent(Builder builder) {
        super(builder);
        this.linkUri = builder.linkUri;
    }

    /**
     * 获取分享链接Uri
     * @return 分享链接Uri
     */
    public Uri getLinkUri() {
        return linkUri;
    }

    /**
     * 分享链接内容容器构建类
     */
    public static class Builder extends ShareContent.Builder<ShareLinkContent, Builder> {
        private Uri linkUri;

        /**
         * 链接Uri（必填）
         * @param linkUri 链接Uri
         * @return
         */
        public Builder setLinkUri(Uri linkUri) {
            this.linkUri = linkUri;
            return this;
        }

        @Override
        public ShareLinkContent build() {
            return new ShareLinkContent(this);
        }
    }
}
