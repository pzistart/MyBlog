package com.pzi.blog.service;

import com.pzi.blog.vo.Result;
import com.pzi.blog.vo.params.LoginParams;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Pzi
 * @create 2022-03-05 14:03
 */
@Transactional
public interface LoginService {

    Result login(LoginParams loginParams);


    Result logout(String token);

    /**
     * 根据前端传回的参数，进行注册
     * @param params
     * @return
     */
    Result register(LoginParams params);

}
