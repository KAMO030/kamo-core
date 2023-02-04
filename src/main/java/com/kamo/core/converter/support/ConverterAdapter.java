package com.kamo.core.converter.support;

import com.kamo.core.converter.Converter;
import com.kamo.core.converter.GenericConverter;
import com.kamo.core.util.ReflectUtils;

import java.util.function.Function;

public class ConverterAdapter implements GenericConverter {

    private Class targetType;

    private Class returnType;
    private Function convertFn;
    public ConverterAdapter(Class targetType, Class returnType, Function convertFn) {
        this.targetType = targetType;
        this.returnType = returnType;
        this.convertFn = convertFn;
    }

    @Override
    public Object convert(Object value) {
        return convertFn.apply(value);
    }

    @Override
    public Class getTargetType() {
        return targetType;
    }

    @Override
    public Class getReturnType() {
        return returnType;
    }
    public static final ConverterAdapter adapter(Converter converter){
        Class targetType = ReflectUtils.getActualTypeOnInterface(converter.getClass(), Converter.class.getName(), 0);
        Class returnType = ReflectUtils.getActualTypeOnInterface(converter.getClass(), Converter.class.getName(), 1);
        Function convertFn = val -> converter.convert(val);
        return new ConverterAdapter(targetType,returnType,convertFn);
    }
}
