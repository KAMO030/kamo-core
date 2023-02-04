package com.kamo.core.mq.example.service;

import com.kamo.core.mq.Message;
import com.kamo.core.mq.Subscribe;
import comment.pojo.Order;

public class RepertoryService {

    /**
     * 自动生成一个[Subscriber]对象,并关注声明的频道
     * [Subscriber]此对象根据注解声明:
     * 关注了[subChannelIDs]为[bilibili]的这一个频道
     * 并且只对该频道主题(title)为[o001]的消息(Message)感兴趣
     * 当发布者向[bilibili]频道发布主题为[o001]的消息时会自动调用该方法
     * @param orderMessage
     * 此方法返回值无意义
     */
    @Subscribe(title = "o001",subChannelIDs = {"bilibili"})
    public void inventoryReduction(Message<Order> orderMessage){
        Order content = orderMessage.getContent();
        String commodityID = content.getCommodityID();
        Integer count = content.getCount();
        System.out.println("关注了[bilibili]频道,对[o001]主题感兴趣的订阅者");
        System.out.println("ID为["+commodityID+"]的商品减少库存["+count+"]个");
    }
}
