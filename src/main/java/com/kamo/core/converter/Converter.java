package com.kamo.core.converter;





/**
 * 转换器
 * @param <T> value原本的类型
 * @param <R> 需要转换到的类型
 */
@FunctionalInterface
public interface Converter<T,R>   {
    R convert(T value);


}
