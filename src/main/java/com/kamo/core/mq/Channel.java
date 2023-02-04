package com.kamo.core.mq;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Channel {
    public static final Channel DEFAULT = new Channel("DEFAULT");

    private final String channelID;
    private final Set<Subscriber> subscribers = new HashSet<>();

    public Channel(String channelID) {
        this.channelID = channelID;
        ChannelManager.addChannel(this);
    }

    public Set<Subscriber> getSubscribers() {
        return subscribers;
    }

    public String getChannelID() {
        return channelID;
    }

    public void subFrom(Subscriber... subs) {
        for (Subscriber subscriber : subs) {
            this.subscribers.add(subscriber);
        }
    }

    public void push(Message message){
        subscribers.stream()
                .filter(subscriber -> subscriber.isInterested(message))
                .forEach(subscriber -> subscriber.accept(message));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Channel channel = (Channel) o;

        return Objects.equals(channelID, channel.channelID);
    }

    @Override
    public int hashCode() {
        return channelID != null ? channelID.hashCode() : 0;
    }


}
