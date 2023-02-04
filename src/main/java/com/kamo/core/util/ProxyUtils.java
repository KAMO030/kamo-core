package com.kamo.core.util;

import com.kamo.core.cglib.ProxyClass;
import com.kamo.core.support.impl.LazedInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.function.Supplier;

public final class ProxyUtils {
    private ProxyUtils() {
    }

    public static <T> T creatProxyInstance(Class<T> targetClass, InvocationHandler handler) {
        return creatProxyInstance(targetClass.getClassLoader(), targetClass, handler);
    }

    public static <T> T creatLazedProxyInstance(Class<T> targetClass, Supplier instanceSupplier) {
        InvocationHandler lazedInvocationHandler = new LazedInvocationHandler(instanceSupplier);
        return creatProxyInstance(targetClass, lazedInvocationHandler);
    }

    public static<T> T creatLazedProxyInstance(ClassLoader classLoader, Class<T> targetClass, Supplier instanceSupplier) {
        InvocationHandler lazedInvocationHandler = new LazedInvocationHandler(instanceSupplier);
        return creatProxyInstance(classLoader, targetClass, lazedInvocationHandler);
    }

    public static <T> T creatProxyInstance(ClassLoader classLoader, Class<T> targetClass, InvocationHandler handler) {
        if (targetClass.isInterface()) {
            return (T) Proxy.newProxyInstance(classLoader, new Class[]{targetClass}, handler);
        }
        Class<?>[] interfaces = targetClass.getInterfaces();

        return interfaces.length > 0 ?
                (T) Proxy.newProxyInstance(classLoader, interfaces, handler) :
                ProxyClass.newProxyInstance(classLoader, targetClass, handler);
    }
}
