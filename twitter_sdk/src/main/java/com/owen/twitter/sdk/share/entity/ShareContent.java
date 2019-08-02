package com.owen.twitter.sdk.share.entity;

/**
 * <br/>Author：yunying.zhang
 * <br/>Email: yinglovezhuzhu@gmail.com
 * <br/>Date: 2019/7/17
 */
public abstract class ShareContent<P extends ShareContent, B extends ShareContent.Builder> {

    private String tag;
    private String text;

    protected ShareContent(Builder builder) {
        this.tag = builder.tag;
        this.text = builder.text;
    }

    public abstract static class Builder<P extends ShareContent, B extends Builder> {

        private String tag;
        private String text;

        public B setTag(String tag) {
            this.tag = tag;
            return (B) this;
        }

        public B setText(String text) {
            this.text = text;
            return (B) this;
        }

        /**
         * 构建分享内容对象
         * @return 分享内容对象
         */
        public abstract P build();
    }
}
