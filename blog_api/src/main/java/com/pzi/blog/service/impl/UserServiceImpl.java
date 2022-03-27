package com.pzi.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.pzi.blog.dao.SysUser;
import com.pzi.blog.dao.mapper.SysUserMapper;
import com.pzi.blog.service.UserService;
import com.pzi.blog.utils.JWTUtils;
import com.pzi.blog.vo.ErrorCode;
import com.pzi.blog.vo.LoginUserVo;
import com.pzi.blog.vo.Result;
import com.pzi.blog.vo.params.LoginParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.sound.midi.SysexMessage;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import java.util.Map;

/**
 * @author Pzi
 * @create 2022-03-01 21:00
 */
@Service
public class UserServiceImpl implements UserService {

//    select xx from ms_sys_user where xxx;
    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

//    根据authorId查询出Author名字nickname
    @Override
    public SysUser selectAuthor(Long authorId) {

        SysUser sysUser = userMapper.selectById(authorId);
        return sysUser;

    }

    /**
     * 根据账户密码查询用户
     * @param account
     * @param pwd
     * @return
     */
    @Override
    public SysUser selectUser(String account, String pwd) {
        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SysUser::getAccount,account);
        lqw.eq(SysUser::getPassword,pwd);
        lqw.select(SysUser::getAccount,SysUser::getId,SysUser::getNickname,SysUser::getAvatar);
        SysUser sysUser = userMapper.selectOne(lqw);
        return sysUser;
    }

    @Override
    public SysUser selectUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SysUser::getAccount,account);
        lqw.last("limit 1");
        SysUser sysUser = userMapper.selectOne(lqw);
        return sysUser;
    }

    @Override
    public void save(SysUser sysUser) {
        userMapper.insert(sysUser);
    }

    @Override
    public LoginUserVo userToLoginUserVo(Long authorId) {
        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SysUser::getId,authorId);
        SysUser sysUser = userMapper.selectOne(lqw);

//        如果根据id找不到author，那么新建一个默认的匿名用户
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("匿名用户");
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(sysUser,loginUserVo);
        return loginUserVo;
    }


}

