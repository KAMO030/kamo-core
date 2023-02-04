package com.kamo.core.util;

import java.util.Collection;
import java.util.Map;

public class ClassUtils {
    public static  ClassLoader getDefaultClassLoader(){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ClassUtils.class.getClassLoader();
        }
        return classLoader;
    }

    public static Class loadClass(ClassLoader classLoader, String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static Class loadClass( String className) {
        return loadClass(getDefaultClassLoader(), className);
    }

    public static boolean isCollection(Class type) {
        return Collection.class.isAssignableFrom(type);
    }

    public static boolean isMap(Class type) {
        return Map.class.isAssignableFrom(type);
    }
}
