package com.kamo.core.mq;

import com.kamo.core.mq.example.service.OrderService;
import com.kamo.core.mq.example.service.RepertoryService;
import com.kamo.core.mq.example.service.UserService;
import comment.pojo.Order;
import comment.pojo.Student;

public class MessageTest {
    public static void main(String[] args) {
//        simp();
        annotationExample();
    }


    private static void annotationExample() {
        AnnotationParser parser = new AnnotationParser();
        Channel blibili = new Channel("bilibili");
        //产生一个[OrderService.class]类型的代理对象
        //此代理对象对方法上有[Publish]注解的方法进行了aop,后置增强
        //会将方法的返回值包装为[Message]对象,并根据[Publish]注解发送到对应频道
        OrderService orderService = parser.proxyPubMethod(OrderService.class);
        //将传入对象打了[Publish]注解的属性按照注解声明自动生成发布者,并赋值给传入对象的对应属性;
        parser.injectPub(orderService);
        //根据传入的Class数组
        // 将打了[Subscribe]注解的方法按照注解的声明自动生成订阅者并关注声明的频道与感兴趣主题
        parser.scanSubs(RepertoryService.class, UserService.class);
        Order order = new Order()
                .setOrderID("o001")
                .setCommodityID("c001")
                .setCount(2)
                .setTotalPrices(9.99)
                .setUserName("zs");
//        orderService.place(order);
        orderService.placeWithAll(order);
    }

    //不用注解,最原始的方式调用
    private static void simp() {
        //        Publisher<String> simplePublisher = new SimplePublisher();
        Channel blibili = new Channel("bilibili");
        Channel nicocnico = new Channel("nicocnico");

//        Subscriber<Integer> subscriber1 = new TitleSubscriber<>("hello");
//        subscriber1.subscribe(m-> System.out.println(m.getContent()));
//        Subscriber<Integer> subscriber2 = new TitleSubscriber<>("hello");
//        subscriber2.subscribe(m-> System.out.println(m.getContent()));
//        blibili.addSubscriber(subscriber1);
//        nicocnico.addSubscriber(subscriber2);
//        simplePublisher.publish(Message.of("hello", "word"));
        nicocnico.subFrom(Subscriber.<Student>byTitle("hello",
                (m) -> System.out.println(m.getContent().getClassName())));
        blibili.subFrom(
                Subscriber.<Student>byTitle("hello",
                        (m) -> System.out.println(m.getContent())));
        Publisher.DEFAULT_PUBLISHER
                .register(nicocnico,blibili)
                .publish(Message.of("hello", new Student()));
    }
}
