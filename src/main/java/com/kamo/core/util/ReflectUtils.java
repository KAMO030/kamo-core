package com.kamo.core.util;


import com.kamo.core.exception.ReflectException;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class ReflectUtils {
    private ReflectUtils() {
    }


    public static Type[] getActualTypeArguments(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            return pt.getActualTypeArguments();
        }
        return new Type[]{type};
    }

    public static <T extends Type> T getActualTypeFirstArgument(T type) {
        return (T) getActualTypeArguments(type)[0];
    }

    public static Type[] getActualTypesOnInterface(Class targetType, String interfaceName) throws IllegalArgumentException {
        Type[] genericInterfaces = targetType.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType && genericInterface.getTypeName().startsWith(interfaceName)) {
                return ReflectUtils.getActualTypeArguments(genericInterface);
            }
        }
        throw new IllegalArgumentException(targetType + " 没有实现：" + interfaceName);
    }

    public static Class getActualTypeOnInterface(Class targetType, String interfaceName) throws IllegalArgumentException {
        return (Class) getActualTypesOnInterface(targetType, interfaceName)[0];
    }
    public static Class getActualTypeOnInterface(Class targetType, String interfaceName,int index) throws IllegalArgumentException {
        return (Class) getActualTypesOnInterface(targetType, interfaceName)[index];
    }
    public static <T> Class getWrapperClass(Class<T> type) {
        if (!type.isPrimitive()) {
            return type;
        }
        String typeName = type.getName();
        if (typeName.equals("byte"))
            return Byte.class;
        if (typeName.equals("short"))
            return Short.class;
        if (typeName.equals("int"))
            return Integer.class;
        if (typeName.equals("long"))
            return Long.class;
        if (typeName.equals("char"))
            return Character.class;
        if (typeName.equals("float"))
            return Float.class;
        if (typeName.equals("double"))
            return Double.class;
        if (typeName.equals("boolean"))
            return Boolean.class;
        if (typeName.equals("void"))
            return Void.class;
        throw new IllegalArgumentException("Not primitive type :" + typeName);
    }

    public static <T> T parseString(Class<T> type, String value) {
        if (!isPrimitive(type)) {
            throw new IllegalArgumentException("[" + type + "]类型不是原始类型或其包装类");
        }
        Object parsed = null;
        if (type == String.class) {
            parsed = value;
        } else if (type == int.class || type == Integer.class) {
            parsed = Integer.parseInt(value);
        } else if (type == float.class || type == Float.class) {
            parsed = Float.parseFloat(value);
        } else if (type == long.class || type == Long.class) {
            parsed = Long.parseLong(value);
        } else if (type == double.class || type == Double.class) {
            parsed = Double.parseDouble(value);
        } else if (type == boolean.class || type == Boolean.class) {
            parsed = Boolean.parseBoolean(value);
        } else if (type == char.class || type == Character.class) {
            parsed = value.charAt(0);
        } else if (type == byte.class || type == Byte.class) {
            parsed = Byte.parseByte(value);
        }
        return (T) parsed;
    }

    public static boolean isPrimitive(Class type) {
        Objects.requireNonNull(type);
        if (type.isPrimitive() || String.class.equals(type)) {
            return true;
        }
        try {
            type.getDeclaredField("TYPE");
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public static Object invokeMethod(Method method, Object instance, Object... args) {
        try {
            Objects.requireNonNull(method);
            method.setAccessible(true);
            return method.invoke(instance, args);
        } catch (Exception e) {
            throw new ReflectException("执行 " + instance + " 的 " + method + " 方法时发生异常,实参为: " + Arrays.toString(args), e);
        }
    }

    public static Object invokeMethod(Object instance, String methodeName, Object... args) {
        Class[] parameterTypes = new Class[args.length];
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = args[i].getClass();
            }
        }
        return invokeMethod(instance, methodeName, parameterTypes, args);
    }

    public static Object invokeMethod(Object instance, String methodeName, Class[] parameterTypes, Object... args) {
        Class instanceClass = Objects.isNull(instance) ? null : instance.getClass();
        Method method = getMethod(instanceClass, methodeName, parameterTypes);
        return invokeMethod(method, instance, args);
    }

    public static Object getFieldValue(Field field, Object instance) {
        try {
            Objects.requireNonNull(field);
            field.setAccessible(true);
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new ReflectException(e);
        }
    }

    public static Object getFieldValue(String fieldName, Object instance) {
        return getFieldValue(getField(instance.getClass(), fieldName, true), instance);
    }

    public static void setFieldValue(Field field, Object instance, Object value) {
        try {
            Objects.requireNonNull(field);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new ReflectException(e);
        }
    }

    public static void setFieldValue(String fieldName, Object instance, Object value) {
        setFieldValue(getField(instance.getClass(), fieldName, true), instance, value);
    }

    public static <T> T newInstance(Constructor<T> constructor, Object... args) {
        try {
            Objects.requireNonNull(constructor);
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }


    public static <T> T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    public static <T> T newInstance(Class<T> type, Object... args) {
        return newInstance(type, null, args);
    }

    public static <T> T newInstance(Class<T> type, Class[] argTypes, Object... args) {
        if (needAutoParserTypes(argTypes, args)) {
            argTypes = getComponentTypes(args);
        }

        Constructor<T> constructor = ReflectUtils.getConstructor(type, argTypes);
        return newInstance(constructor, args);
    }

    public static boolean needAutoParserTypes(Class[] argTypes, Object... args) {
        return (args != null && args.length != 0) && (argTypes == null || argTypes.length != args.length);
    }

    public static Class[] getComponentTypes(Object... args) {
        if (args == null || args.length == 0) {
            return new Class[0];
        }

        Class[] classes = new Class[args.length];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = args[i].getClass();
        }
        return classes;
    }

    public static Method getMethod(Class type, String methodName, boolean isSearchSuper, Class... parameterTypes) {
        if (!isSearchSuper) {
            try {
                return type.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                throw new ReflectException(e);
            }
        } else {
            return forEachSuperclass(type, (Function<Class, Method>) supperClass -> {
                try {
                    return type.getDeclaredMethod(methodName, parameterTypes);
                } catch (NoSuchMethodException e) {
                    return null;
                }
            });
        }
    }

    public static Method getMethod(Class type, String methodName, Class... parameterTypes) {
        return getMethod(type, methodName, false, parameterTypes);
    }

    public static Field getField(Class type, String fieldName, boolean isSearchSuper) {
        if (!isSearchSuper) {
            try {
                return type.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                throw new ReflectException(e);
            }
        } else {
            return forEachSuperclass(type, (Function<Class, Field>) supperClass -> {
                try {
                    return type.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    return null;
                }
            });
        }
    }


    public static Field getField(Class type, String fieldName) {
        return getField(type, fieldName, false);
    }

    /**
     * 遍历type及其父类的所有属性
     *
     * @param type
     * @param predicate test方法返回ture时停止遍历
     */
    public static void forEachField(Class type, Predicate<Field> predicate) {
        forEachMember(type, predicate, Class::getDeclaredFields);
    }

    public static <T> void forEachMember(Class type, Predicate<T> predicate, Function<Class, T[]> getterArray) {
        forEachSuperclass(type, (Predicate<Class>) c ->
                Arrays.stream(getterArray.apply(c)).anyMatch(predicate)
        );
    }

    public static void forEachMethod(Class type, Predicate<Method> predicate) {
        forEachMember(type, predicate, Class::getDeclaredMethods);
    }

    public static void forEachFieldStream(Class type, Predicate<Field> isBreak) {
        //获得ReflectTestProxy.class的父类包括他自己组成的流
        //通过streamField方法将supperClassStream返回的Stream<Class>打平转换成Stream<Field>流
        streamField(supperClassStream(type)).anyMatch(isBreak);
    }
    /**
     * 根据classStream获得流中所有对象的属性所组成的流
     * @param classStream class的流
     * @return classStream流中所有对象的属性所组成的流
     */
    public static Stream<Field> streamField(Stream<Class> classStream) {
        return classStream
                //根据获得的每一个classStream里面的每一个类获得他的所有Field数组stream
                .map(Class::getDeclaredFields)
                //将Field数组stream打平合并成Field组成的stream
                .flatMap(Arrays::stream);
    }
    public static Stream<Field> streamField(Class... classes) {
        return streamField(Stream.of(classes));
    }
    public static Stream<Method> streamMethod(Stream<Class> classStream) {
        return classStream
                //根据获得的每一个classStream里面的每一个类获得他的所有Method数组stream
                .map(Class::getDeclaredMethods)
                //将Field数组stream打平合并成Method组成的stream
                .flatMap(Arrays::stream);
    }

    public static Stream<Method> streamMethod(Class... classes) {
        return streamMethod(Stream.of(classes));
    }
    /**
     * 通过一个class对象获得，他和他所有父类的Stream流，包括Object类
     *
     * @param tClass 目标类
     */
    public static Stream<Class> supperClassStream(Class tClass) {
        //isBreak的条件为只要tClass.getSuperclass的结果不为null且不等于Object就一直获取父类并存入流
        return supperClassStream(tClass, type -> type != null && !type.equals(Object.class));
    }

    /**
     * 通过一个class对象获得，他和他所有父类的Stream流
     *
     * @param tClass  目标类
     * @param isBreak 判断是否停止获得父类加入流
     * @return isBreak.test(type)为ture之前的所有父类所组成的Stream流
     */
    public static Stream<Class> supperClassStream(Class tClass, Predicate<Class> isBreak) {
        return StreamUtils.buildStream(tClass, Class::getSuperclass, isBreak);
    }



    public static void forEachSuperclass(Class type, Predicate<Class> predicate) {
        Objects.requireNonNull(type);
        //predicate没有返回ture并且 (type不等于Object或null)
        while (!predicate.test(type) &&
                (!type.equals(Object.class) || type != null)) {
            type = type.getSuperclass();
        }
    }

    public static <T> T forEachSuperclass(Class type, Function<Class, T> function) {
        Objects.requireNonNull(type);
        while (!type.equals(Object.class) || type != null) {
            T result = function.apply(type);
            if (result != null) {
                return result;
            }
            type = type.getSuperclass();
        }
        return null;
    }

    public static <T> Constructor<T> getConstructor(Class<T> targetClass, Class... parameterTypes) {
        try {
            return targetClass.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new ReflectException(e);
        }
    }

    public static Constructor getConstructor(Class targetClass) {
        return getConstructor(targetClass, new Class[0]);
    }

    public static <T> void forEachArray(T[] array, Consumer<T> consumer) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException(array + " 不是数组类型");
        }
        try {
            for (int i = 0; true; i++) {
                consumer.accept((T) Array.get(array, i));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    public static Object collection2Array(Collection collection) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(collection + " collection为空");
        }
        return array2Array(collection.toArray());
    }

    public static Object[] array2Array(Object[] fromArray) {
        if (fromArray.length < 1) {
            throw new IllegalArgumentException(fromArray + " 数组为空");
        }
        Class componentType = fromArray[0].getClass();
        Object toArray = Array.newInstance(componentType, fromArray.length);
        array2Array(toArray, fromArray);
        return (Object[]) toArray;
    }

    public static void collection2Array(Object array, Collection collection) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException(array + " 不是数组类型");
        }
        array2Array(array, collection.toArray());
    }

    public static void array2Array(Object toArray, Object[] fromArray) {
        if (!toArray.getClass().isArray()) {
            throw new IllegalArgumentException(toArray + " 不是数组类型");
        }
        System.arraycopy(fromArray, 0, toArray, 0, fromArray.length);
    }


}
