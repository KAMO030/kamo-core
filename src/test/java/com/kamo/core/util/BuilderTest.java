package com.kamo.core.util;

import comment.pojo.User;

public class BuilderTest {
    public static void main(String[] args) {
        User user = Builder.create(User::new)
                .and(User ::setUsername,"zs")
//                .and(i->{throw new RuntimeException("");})
//                .and(User ::setPassword,"123")
//                .catchThrow(e->{
//                    System.out.println(e);
//                })
                .verify(u -> u.getPassword()!=null)
                .build();

        System.out.println(user);

    }
}
