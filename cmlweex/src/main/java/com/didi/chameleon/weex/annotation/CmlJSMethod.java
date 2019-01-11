package com.didi.chameleon.weex.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Cml 方法注解
 * limeihong
 * create at 2018/10/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface CmlJSMethod {
    boolean uiThread() default true;

    String alias() default NOT_SET;

    String NOT_SET = "_";
}
