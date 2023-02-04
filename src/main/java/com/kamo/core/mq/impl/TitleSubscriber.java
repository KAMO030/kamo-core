package com.kamo.core.mq.impl;

import com.kamo.core.mq.Message;

import java.util.function.Consumer;

public class TitleSubscriber<T> extends AbstractSubscriber<T> {
    private final String subTitle;

    public TitleSubscriber(String subTitle) {
        this.subTitle = subTitle;
    }

    public TitleSubscriber(String subTitle, Consumer<Message<T>> messageConsumer) {
        super(messageConsumer);
        this.subTitle = subTitle;
    }

    @Override
    public boolean isInterested(Message<T> message) {
        return subTitle.equals(message.getTitle());
    }
}
