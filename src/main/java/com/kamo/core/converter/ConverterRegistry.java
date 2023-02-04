package com.kamo.core.converter;


import com.kamo.core.converter.support.ConverterAdapter;
import com.kamo.core.converter.support.StringDateConverter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConverterRegistry {
    private static final GenericConverter[] EMPTY_CONVERTER_ARRAY = new GenericConverter[0];
    protected Map<Class, Set<GenericConverter>> converterMap = new ConcurrentHashMap<>();
    public ConverterRegistry() {
        this(EMPTY_CONVERTER_ARRAY);
    }

    public ConverterRegistry(ConverterRegistry registry) {
        this(registry.getConverters());
    }
    public ConverterRegistry(Collection<GenericConverter> converters) {
        this(converters.toArray(EMPTY_CONVERTER_ARRAY));
    }
    public ConverterRegistry(GenericConverter ... converters) {
        initDefaultConverter();
        for (GenericConverter converter : converters) {
            this.registerConverter(converter);
        }
    }
    private void initDefaultConverter() {
        registerConverter(ConverterAdapter.adapter(new StringDateConverter()));
    }
    public  void registerConverter(GenericConverter converter) {
        Class targetType = converter.getTargetType();
        Set converterSet;
        if (converterMap.containsKey(targetType)) {
            converterSet = converterMap.get(targetType);
        } else {
            converterSet = new HashSet();
            converterMap.put(targetType, converterSet);
        }
        converterSet.add(converter);
    }

    public boolean isRegister(Class returnType, Class targetType) {
        return getConverter(returnType, targetType) != null;
    }
    public  GenericConverter getConverter(Class returnType, Class targetType) {
        if (!converterMap.containsKey(targetType)) {
            return null;
        }
        Set<GenericConverter> converterSet = converterMap.get(targetType);
        for (GenericConverter converter : converterSet) {
            Class rType = converter.getReturnType();
            if (rType != null && rType.isAssignableFrom(returnType)) {
                return converter;
            }
        }
        return null;
    }

    public GenericConverter[] getConverters() {
        return converterMap.isEmpty() ?
                EMPTY_CONVERTER_ARRAY : this.converterMap.values().toArray(EMPTY_CONVERTER_ARRAY);
    }


}
