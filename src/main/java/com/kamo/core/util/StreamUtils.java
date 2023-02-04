package com.kamo.core.util;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class StreamUtils {

    private StreamUtils(){}
    public static <T> Stream<T> buildStream(T src, Function<T,T> updateVar, Predicate<T> predicate) {
        Stream.Builder<T> builder = Stream.builder();
        T newVar = src;
        //通过新值判断是否继续添加流的元素
        while (predicate.test(newVar)){
            //把原始的值作为第一个传入流
            builder.add(newVar);
            //根据老值获得新值，更新引用
            newVar = updateVar.apply(newVar);
        }
        return builder.build();
    }

}
