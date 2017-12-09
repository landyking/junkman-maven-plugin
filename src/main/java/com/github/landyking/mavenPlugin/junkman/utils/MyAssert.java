package com.github.landyking.mavenPlugin.junkman.utils;

/**
 * Created by landy on 2017/12/9.
 */
public class MyAssert {
    public static void notNull(Object val) {
        if (val == null) {
            throw new IllegalArgumentException("value is null");
        }
    }
}
