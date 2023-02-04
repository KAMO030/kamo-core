package com.kamo.core.mq;

import com.kamo.core.mq.impl.SimplePublisher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Publish {
    String[] subChannelIDs() default {"DEFAULT"};

    Class<? extends Publisher> publisherClass() default SimplePublisher.class;

    String messageTitle ()default "DEFAULT_TITLE";
}
