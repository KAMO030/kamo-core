package com.kamo.core.util;

import comment.pojo.Student;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ReflectTest {
    public static void main(String[] args) {
//        getAndSetField();
        forEach();
//        newInstance();
//        getActualTypeArgument();
    }

    public static void getActualTypeArgument() {

        //获得子类定义的父类泛型,如有多个返回第一个
        Class actualTypClass = (Class)ReflectUtils.getActualTypeFirstArgument(Student.class.getGenericSuperclass());
        //

        //获得子类定义的父类所有泛型
        Type[] actualTypeArguments = ReflectUtils.getActualTypeArguments(Student.class.getGenericSuperclass());
        //获得子类实现的接口的泛型
        Class actualTypeInterface = (Class)ReflectUtils.getActualTypeFirstArgument( Student.class.getGenericInterfaces()[0]);

        System.out.println(actualTypClass);
        System.out.println(Arrays.toString(actualTypeArguments));
        System.out.println(actualTypeInterface);
    }

    public static void getAndSetField(){
        //作用：通过class对象获得一个名字叫某某的属性
        //不写第三个参数默认只在本类搜索，因为传入的Student.class并没有叫age的属性所以会抛出异常
//        Field field = ReflectUtils.getField(Student.class, "age", false);
        //写了第三个参数为ture以后就也会到父类搜索所以不会为null,不会抛出异常
        Field field = ReflectUtils.getField(Student.class, "age", true);
        Student student = new Student();
        ReflectUtils.setFieldValue("age",student,13);
        System.out.println(field);
        System.out.println(ReflectUtils.getFieldValue("age",student));
    }

    public static void newInstance() {
        //调用的构造参数类型顺序须一致
        //无参 Student()
//        Student student = ReflectUtils.newInstance(Student.class);
        //半参 Student(String name, Integer age)
        Student student = ReflectUtils.newInstance(Student.class,"zs",18);
        //全参 Student(String sno, String className,String name, Integer age)
//        Student student = ReflectUtils.newInstance(Student.class,"s001","c001","zs",18);
        System.out.println(student);
    }
    public static void forEach(){
        //作用：遍历传入的类型及其父类的所有属性并进行打印，如果属性名等于某个值终止遍历
        //终止遍历的属性名
        String stopName = "name";
        ReflectUtils.forEachFieldStream(Student.class, field -> {
            String fieldName = field.getName();
            System.out.println("stream---"+fieldName);
            return fieldName.equals(stopName);
        });
        //作用同上，第一个简单的不用stream的版本
        ReflectUtils.forEachField(Student.class,field -> {
            String fieldName = field.getName();
            System.out.println("forEach---"+fieldName);
            return fieldName.equals(stopName);
        });
        //作用：验证对象的属性（包括父类）是否为空如果为空抛出异常
//        Student proxy = new Student();
//        ReflectUtils.forEachField(Student.class,field -> {
//            field.setAccessible(true);
//            Object var = null;
//            try {
//                var = field.get(proxy);
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//            if (var == null){
//                throw new IllegalArgumentException("对象的"+field.getName()+"属性为空为空");
//            }
//            return Boolean.FALSE;
//        });

    }

}
