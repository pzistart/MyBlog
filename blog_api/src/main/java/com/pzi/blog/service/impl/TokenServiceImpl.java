package com.pzi.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.pzi.blog.dao.SysUser;
import com.pzi.blog.service.TokenService;
import com.pzi.blog.utils.JWTUtils;
import com.pzi.blog.vo.ErrorCode;
import com.pzi.blog.vo.LoginUserVo;
import com.pzi.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Pzi
 * @create 2022-03-05 16:27
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Result getUserByToken(String token) {
//        token中保存了json字符串格式的user信息，校验通过后，解析json字符串，就拿到了SysUser对象
//        校验token
//        1、检查token中是否有值
//        checkToken可以得到当初的userId
        Map<String, Object> map = JWTUtils.checkToken(token);
        if (map == null) {
            System.out.println("token检查失败，里面不含信息");
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }

//        2、去redis中根据该token(key)取value，判断是否为空
        String userStr = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isEmpty(userStr)) {
            System.out.println("token已失效");
            return Result.fail(ErrorCode.TOKEN_OUTDATE.getCode(), ErrorCode.TOKEN_OUTDATE.getMsg());
        }

//        查看一下 redis 中的 userStr怎么样的
        System.out.println(userStr);
//        3、校验通过，解析json字符串 ==> SysUser对象
        SysUser sysUser = JSON.parseObject(userStr,SysUser.class);

        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setNickname(sysUser.getNickname());

        return Result.success(loginUserVo);
    }

    @Override
    public SysUser checkTokenAll(String token) {

//        1、判断从请求头中拿到的token是否为空，若空表示未登录
//        2、检查token是否有对应值
//        3、去redis中根据该token(key)取value，判断是否为空
//        4、校验通过，解析json字符串 ==> SysUser对象

        if (StringUtils.isBlank(token)){
            return null;
        }

        Map<String, Object> map = JWTUtils.checkToken(token);
        if (map == null){
            return null;
        }

        String sysUserString = redisTemplate.opsForValue().get("TOKEN_" + token);

        if (StringUtils.isBlank(sysUserString)) {
            return null;
        }

        SysUser sysUser = JSON.parseObject(sysUserString, SysUser.class);
        return sysUser;
    }
}

