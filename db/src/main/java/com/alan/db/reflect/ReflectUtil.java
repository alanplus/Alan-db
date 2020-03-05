package com.alan.db.reflect;

/**
 * @author Alan
 * 时 间：2019-12-18
 * 简 述：<功能简述>
 */
public class ReflectUtil {

    public static <T> Class<T> getTClass() {
        return (new PatternClass<T>() {
        }).getTClass();
    }

}
