package com.kamo.core.mq;

public class Message<T>{
    private final String title;

    private final T content;

    public Message(String title, T content) {
        this.title = title;
        this.content = content;
    }

    public  static <T> Message<T> of(String title,T content){
        return new Message<>(title, content);
    }

    public String getTitle() {
        return title;
    }

    public T getContent() {
        return content;
    }
}
