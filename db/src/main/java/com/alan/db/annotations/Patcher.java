package com.alan.db.annotations;


import com.alan.db.base.IPatcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Alan
 * 时 间：2019-11-26
 * 简 述：<功能简述>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Patcher {

    Class<? extends IPatcher>[] name();
}
