package com.pzi.blog.commmon.aop;


import java.lang.annotation.*;

/**
 * @author Pzi
 * @create 2022-03-08 21:36
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";

    String operation() default "";

}
