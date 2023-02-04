package com.kamo.core.mq.impl;

import com.kamo.core.mq.Message;
import com.kamo.core.mq.Subscriber;
import com.kamo.core.util.ReflectUtils;

import java.lang.reflect.Method;

public abstract class AbsSubMethodAdapter<T> implements Subscriber<T> {

    private final Method acceptMethod;
    private final  Object instance;

    private Class<T> msgType;



    public AbsSubMethodAdapter(Method method, Object instance) {
        this.acceptMethod = method;
        this.instance = instance;
    }



    @Override
    public void accept(Message message) {
        ReflectUtils.invokeMethod(acceptMethod,instance,message);
    }
}
