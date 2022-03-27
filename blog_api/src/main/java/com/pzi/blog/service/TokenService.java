package com.pzi.blog.service;

import com.pzi.blog.dao.SysUser;
import com.pzi.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author Pzi
 * @create 2022-03-05 16:26
 */
public interface TokenService {
    Result getUserByToken(String token);

    /**
     * 根据传入的token，检查该token是否有对应的sysUser
     * @param token
     * @return
     */
    SysUser checkTokenAll(String token);
}
