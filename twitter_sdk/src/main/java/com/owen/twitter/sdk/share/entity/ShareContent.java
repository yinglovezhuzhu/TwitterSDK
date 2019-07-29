package com.owen.twitter.sdk.share.entity;

/**
 * <br/>Author：yunying.zhang
 * <br/>Email: yinglovezhuzhu@gmail.com
 * <br/>Date: 2019/7/17
 */
public abstract class ShareContent<P extends ShareContent, B extends ShareContent.Builder> {

    protected ShareContent(Builder builder) {

    }

    public abstract static class Builder<P extends ShareContent, B extends Builder> {

        /**
         * 构建分享内容对象
         * @return 分享内容对象
         */
        public abstract P build();
    }
}
