package com.kamo.core.mq.example.service;

import com.kamo.core.mq.Message;
import com.kamo.core.mq.Publish;
import com.kamo.core.mq.Publisher;
import comment.pojo.Order;

public class OrderService {


    @Publish
    private Publisher<Order> defaultPublisher;
    //根据
    @Publish(subChannelIDs = {"bilibili"})
    private Publisher<Order> bilibiliPublisher;

    /**
     * 测试通过反射注入的Publisher属性
     * @param order
     */
    public void place(Order order){
        //给[DEFAULT]频道发送主题(Title)为[order.getOrderID()],内容(Content)为order对象的消息(Message)对象
        defaultPublisher.publish(Message.of(order.getOrderID(),order));
        //给[bilibili]频道发送主题(Title)为[order.getOrderID()],内容(Content)为order对象的消息(Message)对象
//        bilibiliPublisher.publish(Message.of(order.getOrderID(),order));
    }
    /**
     * 此处使用了代理对象进行了aop
     * 调用此方法后关注了subChannelID为[bilibili]的频道
     * 并对messageTitle为[o001]感兴趣的订阅者
     * 会收到返回值作为Content的Message对象
     * @param order
     * @return 返回值将会作为Message对象的Content属性
     */
    @Publish(messageTitle = "o001",subChannelIDs = {"bilibili"})
    public Order placeWithBilibili(Order order){
        return order;
    }
    /**
     * 此处使用了代理对象进行了aop
     * 调用此方法后关注了subChannelID为[bilibili]和[DEFAULT(注解默认值)]的频道
     * 并对messageTitle为[o001]感兴趣的订阅者
     * 会收到返回值作为Content的Message对象
     * @param order
     * @return 返回值将会作为Message对象的Content属性
     */
    @Publish(messageTitle = "o001",subChannelIDs = {"bilibili","DEFAULT"})
    public Order placeWithAll(Order order){
        return order;
    }

}
