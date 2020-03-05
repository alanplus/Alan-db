package com.alan.db.reflect;

import java.lang.reflect.ParameterizedType;

/**
 * @author Alan
 * 时 间：2019-12-18
 * 简 述：<功能简述>
 */
public class PatternClass<T> {

    public Class<T> getTClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
