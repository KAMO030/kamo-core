package com.kamo.core.mq;

import com.kamo.core.mq.impl.SimplePublisher;

public interface Publisher<T> {


    Publisher DEFAULT_PUBLISHER = new SimplePublisher<>().register(Channel.DEFAULT);
    void publish(Message<T> message);

    Publisher<T> register(Channel... channels);

}
