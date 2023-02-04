package com.kamo.core.mq;

import com.kamo.core.cglib.MethodProxy;
import com.kamo.core.support.AnnotationMetadata;
import com.kamo.core.util.AnnotationUtils;
import com.kamo.core.util.ProxyUtils;
import com.kamo.core.util.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class AnnotationParser {
    private final Map<Class, Object> instancesMapper = new ConcurrentHashMap<>();
    private final Map<Class, Object> proxyMapper = new ConcurrentHashMap<>();

    public void scanSubs(Class<?>... tClasses) {
        for (Class<?> tClass : tClasses) {
            doScan(tClass);
        }
    }

    public void injectPub(Object instance) {
        ReflectUtils.streamField(ReflectUtils.supperClassStream(instance.getClass()))
                .filter(field -> AnnotationUtils.isAnnotationPresent(field, Publish.class))
                .forEach(field -> pubFieldHandler(field, instance));
    }

    public <T> T proxyPubMethod(Class<T> tClass) {
        return ProxyUtils.creatProxyInstance(tClass, new PubMethodHandler(tClass));
    }

    private static class PubMethodHandler implements InvocationHandler {
        private final Map<String, Publisher> publisherMapper = new HashMap<>();
        private final Map<String, String> titleMapper = new HashMap<>();

        private PubMethodHandler(Class tClass) {
            init(tClass);
        }

        private void init(Class tClass) {
            ReflectUtils.streamMethod(ReflectUtils.supperClassStream(tClass))
                    .filter(m -> AnnotationUtils.isAnnotationPresent(m, Publish.class))
                    .map(m -> AnnotationUtils.getAnnotationMetadata(m, Publish.class))
                    .forEach(this::metaHandler);
        }

        private void metaHandler(AnnotationMetadata<Publish> meta) {
            Publisher publisher = getPubByAnnotation(meta);
            Method srcMethod = (Method) meta.getSrcAnnotationElement();
            String methodKey = srcMethod.toString();
            publisherMapper.put(methodKey, publisher);
            titleMapper.put(methodKey, meta.getAnnotationValue("messageTitle"));
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodKey = method.toString();
            Publisher publisher = publisherMapper.get(methodKey);
            Object result = MethodProxy.invokeSuper(proxy, method, args);
            if (publisher != null && result != null) {
                publisher.publish(Message.of(titleMapper.get(methodKey),result));
            }
            return result;
        }
    }

    private static Publisher getPubByAnnotation(AnnotationMetadata<Publish> metadata) {
        Class<Publisher> publisherClass = metadata.getAnnotationValue("publisherClass");
        Publisher publisher = ReflectUtils.newInstance(publisherClass);
        String[] subChannelNames = metadata.getAnnotationValue("subChannelIDs");
        Arrays.stream(subChannelNames)
                .map(ChannelManager::getChannel)
                .filter(Objects::nonNull)
                .forEach(subChan -> publisher.register(subChan));
        return publisher;
    }

    private void pubFieldHandler(Field field, Object instance) {
        AnnotationMetadata<Publish> metadata = AnnotationUtils.getAnnotationMetadata(field, Publish.class);
        Publisher publisher = getPubByAnnotation(metadata);
        ReflectUtils.setFieldValue(field, instance, publisher);
    }

    private void doScan(Class tClass) {
        ReflectUtils.streamMethod(Stream.of(tClass))
                .filter(m -> AnnotationUtils.isAnnotationPresent(m, Subscribe.class))
                .forEach(method -> subMethodHandler(method, tClass));
    }

    private void subMethodHandler(Method method, Class tClass) {
        AnnotationMetadata<Subscribe> metadata = AnnotationUtils.getAnnotationMetadata(method, Subscribe.class);
        Class adapterClass = metadata.getAnnotationValue("adapterClass");
        Object instance = instancesMapper.get(tClass);
        if (instance == null) {
            instance = ReflectUtils.newInstance(tClass);
            instancesMapper.put(tClass, instance);
        }
        Object adapter = ReflectUtils.newInstance(adapterClass, new Class[]{Method.class, Object.class}, metadata.getSrcAnnotationElement(), instance);
        String[] subChannelNames = metadata.getAnnotationValue("subChannelIDs");
        Arrays.stream(subChannelNames)
                .map(ChannelManager::getChannel)
                .filter(Objects::nonNull)
                .forEach(channel -> channel.subFrom((Subscriber) adapter));
    }
}
