package com.kamo.core.mq;

import com.kamo.core.mq.impl.TitleSubscriber;

import java.util.function.Consumer;

public interface Subscriber<T> {



    boolean isInterested(Message<T>  message);


    default Subscriber<T> subscribe(Channel channel){
        channel.subFrom(this);
        return this;
    }



    void accept( Message<T> message);
    static <T> Subscriber<T> byTitle(String title, Consumer<Message<T>> msgConsumer){
        return new TitleSubscriber<>(title,msgConsumer);
    }

}
