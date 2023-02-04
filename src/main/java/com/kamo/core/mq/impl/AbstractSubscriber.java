package com.kamo.core.mq.impl;

import com.kamo.core.mq.Message;
import com.kamo.core.mq.Subscriber;

import java.util.function.Consumer;

public abstract   class AbstractSubscriber<T> implements Subscriber<T> {

    private Consumer<Message<T>> messageConsumer;

    public AbstractSubscriber() {
        this.messageConsumer = m -> {};
    }

    public AbstractSubscriber( Consumer<Message<T>> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }



    @Override
    public void accept(Message<T> message) {
        messageConsumer.accept(message);
    }
}
