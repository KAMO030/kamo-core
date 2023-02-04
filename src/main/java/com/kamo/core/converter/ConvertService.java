package com.kamo.core.converter;

import com.kamo.core.converter.support.ConverterAdapter;
import com.kamo.core.util.ReflectUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ConvertService{

    private ConverterRegistry registry;

    public ConvertService() {
        this.registry = new ConverterRegistry();
    }

    public ConvertService(ConverterRegistry registry) {
        this.registry = registry;
    }

    /**
     * @param target     需要转换的对象实例
     * @param returnType 需要转换到的类型
     * @param <T>        转换后的类型
     * @param <R>        转换前的类型
     *                   如果传入的 #target 是数组而 #returnType 的类型不是数组则只会转换 #target 的第一个元素
     *                   如果传入的 #target 不是数组类型但 #returnType 是数组类型 时可能抛出无法转换异常
     *                   如果传入的 #target 是数组而 #returnType 的类型也是数组则会把 #target 数组的每一个元素转换成 #returnType 数组类型的具体类型然后封装到一个 #returnType类型的数组里返回
     * @return 转换后的对象实例
     * @throws IllegalArgumentException 当没有对应支持的转换器时抛出
     */

    public <T, R> R convert(T target, Class<R> returnType) {
        Class targetType = target.getClass();
        if (targetType.isArray()) {
            return returnType.isArray() ?
                    (R) doConvertArray((T[]) target, (Class<R[]>) returnType, targetType) :
                    (R) doConvert(Array.get(target, 0), returnType, targetType.getComponentType());
        }
        return (R) doConvert(target, returnType, targetType);
    }

    public <T, R> R[] doConvertArray(T[] target, Class<R[]> returnType, Class<T[]> targetType) {

        List<R> partList = new ArrayList<>();
        Class<R> returnComponentType = (Class<R>) returnType.getComponentType();
        Class<T> targetComponentType = (Class<T>) targetType.getComponentType();
        ReflectUtils.forEachArray(target,t->
                partList.add(doConvert(t, returnComponentType, targetComponentType)));
        R[] array = (R[]) Array.newInstance(returnComponentType,0);
        return partList.toArray(array);
    }

    private  <T, R> R doConvert(T target, Class<R> returnType, Class<T> targetType) {
        if (ReflectUtils.isPrimitive(returnType) && ReflectUtils.isPrimitive(targetType)) {
            if (targetType.equals(String.class)) {
                return ReflectUtils.parseString(returnType, (String) target);
            }

            Class returnTypeWrapper = ReflectUtils.getWrapperClass(returnType);
            Method valueOfMethod = ReflectUtils.getMethod(returnTypeWrapper, "valueOf", String.class);

            return (R) ReflectUtils.invokeMethod(null, valueOfMethod, target.toString());
        }
        returnType = ReflectUtils.getWrapperClass(returnType);
        targetType = ReflectUtils.getWrapperClass(targetType);
        Converter<T, R> converter = registry.getConverter(returnType, targetType);
        if (converter != null) {
            return converter.convert(target);
        }
        if (returnType.equals(String.class)) {
            return (R) target.toString();
        }
        throw new IllegalArgumentException("无法将: " + targetType + " 类型转换到: " + returnType + " 类型");
    }

    public  void registerConverter(Converter converter) {
        registry.registerConverter(ConverterAdapter.adapter(converter));
    }
    public  void registerConverter(GenericConverter converter) {
        registry.registerConverter(converter);
    }
    public <T,R> void registerConverter(Class<T> targetType, Class<R> returnType, Function<T,R> convertFn) {
        registerConverter(new ConverterAdapter(targetType,returnType,convertFn));
    }
}
