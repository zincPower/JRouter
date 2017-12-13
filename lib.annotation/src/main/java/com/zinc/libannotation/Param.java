package com.zinc.libannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/12
 * @description
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Param {

    String key() default "";

}
