package com.kamo.core.converter;

import comment.pojo.Person;
import comment.pojo.User;

import java.util.Arrays;
import java.util.Date;

public class ConverterTest {
    public static void main(String[] args) {
        //默认转换器使用
//        simp();
        //实现接口自定义转换器使用
//        custom();
        //lambda自定义转换器使用
//        lambdaCustom();
        //引用类型间的转换
        ObjToObjCustom();
    }


    private static void simp() {

        ConvertService service = new ConvertService();
        StringToUserConverter converter = new StringToUserConverter();
        //通过内置转换器将string转换成int类型
        int i = service.convert("2313131", int.class);
        System.out.println(i);

        //通过内置转换器将string转换成Date类型
        Date date = service.convert("2012-1-23", Date.class);
        System.out.println(date);

        //通过自定义转换器将string数组转换成int类型数组
        Integer[] array = service.convert(new String[]{"123", "456", "678"}, Integer[].class);
        System.out.println(Arrays.toString(array));
    }

    private static void custom() {

        ConvertService service = new ConvertService();

        //注册实现了Converter接口的自定义转换器
        StringToUserConverter converter = new StringToUserConverter();
        service.registerConverter(converter);

        //com.kamo.core.converter.Converter<java.lang.String, comment.pojo.User>
        //注册自定义转换器,Converter转换器不能用lambda，因为lambda无法通过反射获取泛型信息
        //底层是通过泛型获取的原始类型和目标类型
        System.out.println(converter.getClass().getGenericInterfaces()[0]);

        //通过自定义转换器将string转换成user类型
        User user = service.convert("zs,2313131", User.class);
        System.out.println(user);

        //通过自定义转换器将string数组转换成user类型数组
        User[] users = service.convert(new String[]{"zs,2313131", "ls,2313131", "ww,2313131"}, User[].class);
        System.out.println(Arrays.toString(users));

    }

    static class StringToUserConverter implements Converter<String, User> {
        @Override
        public User convert(String value) {
            User user = new User();
            String[] values = value.split(",");
            user.setUsername(values[0]);
            user.setPassword(values[1]);
            return user;
        }
    }

    private static void lambdaCustom() {

        ConvertService service = new ConvertService();
        //使用lambda注册一个将String类型转换成Person类型的自定义转换器
        service.registerConverter(String.class, Person.class, s -> {
            Person person = new Person();
            //将传入字符串["zs-18"]按照[-]分隔
            String[] split = s.split("-");
            //数组第一个作为Name，第二个元素作为Age，并为person赋值
            person.setName(split[0])
                    .setAge(Integer.valueOf(split[1]));
            return person;
        });

        //将String类型转换成Person类型;
        Person person = service.convert("zs-18", Person.class);

        System.out.println(person);

    }
    private static void ObjToObjCustom() {
        ConvertService service = new ConvertService();
        //使用lambda注册一个将User类型转换成Person类型的自定义转换器
        service.registerConverter(User.class, Person.class, u ->
            //user的Username作为Name，Password作为Age，并为person赋值
                // lambda简写了
             new Person().setName(u.getUsername())
                    .setAge(Integer.valueOf(u.getPassword())));

        //将User类型转换成Person类型;
        User target = new User().setUsername("zs").setPassword("123");
        Person person = service.convert(target, Person.class);

        System.out.println(person);
    }


}
