package com.kamo.core.mq;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ChannelManager {
    private ChannelManager(){}


    private static final Map<String,Channel> CHANNELS = new ConcurrentHashMap<>();


    public static void addChannel(Channel channel) {
        CHANNELS.put(channel.getChannelID(), channel);
    }
    public static Channel getChannel(String id){
        return CHANNELS.get(id);
    }

}
