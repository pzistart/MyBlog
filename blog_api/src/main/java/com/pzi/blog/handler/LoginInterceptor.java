package com.pzi.blog.handler;

import com.alibaba.fastjson.JSON;
import com.pzi.blog.dao.SysUser;
import com.pzi.blog.service.TokenService;
import com.pzi.blog.utils.JWTUtils;
import com.pzi.blog.utils.UserThreadLocal;
import com.pzi.blog.vo.ErrorCode;
import com.pzi.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Pzi
 * @create 2022-03-06 16:24
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

//    当访问需要登录才能进行访问的请求(比如评论功能)时，拦截器会判断是否放行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

//        如果前端请求的不是controller方法，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

//        从请求参数的请求头中获取token，如果它为空，则拦截
        String token = request.getHeader("Authorization");

//        控制台打印请求信息
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

//        1、判断从请求头中拿到的token是否为空，若空表示未登录
//        2、判断在redis中该token对应的value是否为空，value若空则说明token失效。表示未登录
        SysUser sysUser = tokenService.checkTokenAll(token);
        if (sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

//        如果能不被拦截，那么UserThreadLocal这个线程中就会存储着当前登录的用户
//          其他成功通过了拦截器的接口中，可以调用get方法得到此sysUser对象
        UserThreadLocal.put(sysUser);
        return true;
    }

//    拦截成功则直接执行下面的方法
//    放行的话，则执行完后相应的controller才执行下面的方法
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}

