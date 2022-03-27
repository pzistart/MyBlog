package com.pzi.blog.utils;

import com.pzi.blog.dao.SysUser;
import org.omg.CORBA.PRIVATE_MEMBER;

/**
 * @author Pzi
 * @create 2022-03-06 19:10
 */
public class UserThreadLocal {

//    给该类的构造器设置成private，那么外部的其他类不能new 该对象；只能调用下面的静态属性；
    private UserThreadLocal(){}

    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    public static SysUser get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }

}

