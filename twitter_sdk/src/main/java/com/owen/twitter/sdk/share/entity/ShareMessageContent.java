package com.owen.twitter.sdk.share.entity;

/**
 * 分享文本消息内容容器
 * <br/>Author：yunying.zhang
 * <br/>Email: yinglovezhuzhu@gmail.com
 * <br/>Date: 2019/7/17
 */
public final class ShareMessageContent extends ShareContent<ShareMessageContent, ShareMessageContent.Builder> {

    private final String messageContent;

    protected ShareMessageContent(Builder builder) {
        super(builder);
        this.messageContent = builder.messageContent;
    }

    /**
     * 获取文本消息内容
     * @return 文本消息内容
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * 分享内容容器构建类
     */
    public static class Builder extends ShareContent.Builder<ShareMessageContent, Builder> {

        private String messageContent;

        /**
         * 设置分享的消息内容
         * @param messageContent 消息内容
         * @return
         */
        public Builder setMessageContent(String messageContent) {
            this.messageContent = messageContent;
            return this;
        }

        @Override
        public ShareMessageContent build() {
            return new ShareMessageContent(this);
        }
    }
}
