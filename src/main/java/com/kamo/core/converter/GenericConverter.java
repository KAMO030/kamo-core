package com.kamo.core.converter;





/**
 * 转换器
 * @param <T> value原本的类型
 * @param <R> 需要转换到的类型
 */

public interface GenericConverter<T,R>  extends Converter<T,R>  {

    Class<T> getTargetType();

    Class<R> getReturnType();


}