package com.pzi.blog.commmon.aop;

import java.lang.annotation.*;

/**
 * @author Pzi
 * @create 2022-03-09 20:48
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    long expire() default 1 * 60 * 1000;// 1000毫秒（1秒） * 60 = 60 秒 = 1分钟

    String name() default "";

}
