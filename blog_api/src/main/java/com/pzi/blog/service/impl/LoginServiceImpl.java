package com.pzi.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.pzi.blog.dao.SysUser;
import com.pzi.blog.service.UserService;
import com.pzi.blog.utils.JWTUtils;
import com.pzi.blog.vo.ErrorCode;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.pzi.blog.service.LoginService;
import com.pzi.blog.vo.Result;
import com.pzi.blog.vo.params.LoginParams;
import org.springframework.beans.PropertyAccessException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import java.util.concurrent.TimeUnit;

/**
 * @author Pzi
 * @create 2022-03-05 14:03
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    private static final String salt = "mszlu!@#";

//    在登录业务中，调用用户mapper，查询数据库信息
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public Result login(LoginParams loginParams) {

        String account = loginParams.getAccount();
        String password = loginParams.getPassword();

//        1、查看参数是否合理 看参数是否为 "" / null
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }

//      从页面传回来的密码经过md5加密(同时加上加密盐)，然后去到数据库中验证
        String pwd = DigestUtils.md5Hex(password+salt);

//        2、使用mapper去数据库中查找有无该user
        SysUser sysUser = userService.selectUser(account,pwd);
        if (sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

//        3、创建token，根据id创建
        String token = JWTUtils.createToken(sysUser.getId());

        log.info("~~~~~~~~~~~~");
        log.info("token~~~ {}",token);
//        3、把 {token --> User的字符串形式} 存到Redis中
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);

    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    @Override
    public Result register(LoginParams params) {
        String account = params.getAccount();
        String password = params.getPassword();
        String nickname = params.getNickname();

//        1、校验参数是否合法
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)
                || StringUtils.isBlank(nickname)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }

//        2、根据account去数据库查找，是否存在该用户，存在则返回fail
        SysUser sysUser = userService.selectUserByAccount(account);
        if(sysUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }

//        3、不存在则将该信息保存到数据库中
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
//        经过md5和加密盐salt，将密码保存到数据库中
        sysUser.setPassword(DigestUtils.md5Hex(password+salt));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        userService.save(sysUser);

//        4、将该信息保存到redis中

        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_" + token,JSON.toJSONString(sysUser),1,TimeUnit.DAYS);
        return Result.success(token);
    }

    public static void main(String[] args) {
        System.out.println(DigestUtils.md5Hex("admin"+salt));
    }
}
