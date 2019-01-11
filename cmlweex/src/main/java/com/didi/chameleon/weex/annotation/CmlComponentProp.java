package com.didi.chameleon.weex.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Cml 控件注解
 * Created by youzicong on 2018/10/18
 */
@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface CmlComponentProp {
    String name();
}

