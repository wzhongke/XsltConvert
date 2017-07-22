package com.learn;

import java.util.Arrays;

/**
 * Created by admin on 2017/7/18.
 */
public class Lambda {

    private static void lambda() {
        Arrays.asList("1", "2", "3").forEach(System.out::println);
    }

    public static void main(String[] arg) {
        lambda();
    }
}

@FunctionalInterface
interface FunctionalMethods {
    void method();
    boolean equals(Object o);
    default void defaultMethod () {

    }
}

interface DefaultInterface {
    default String defaultMethod() {
        return "Default method";
    }
}