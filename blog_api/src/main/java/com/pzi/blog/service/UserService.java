package com.pzi.blog.service;

import com.pzi.blog.dao.SysUser;
import com.pzi.blog.vo.LoginUserVo;
import com.pzi.blog.vo.Result;
import com.pzi.blog.vo.params.LoginParams;

/**
 * @author Pzi
 * @create 2022-03-01 20:59
 */
public interface UserService {

    SysUser selectAuthor(Long authorId);

    SysUser selectUser(String account, String pwd);

    /**
     * 根据account查找用户user
     * @param account
     * @return
     */
    SysUser selectUserByAccount(String account);

    /**
     * 将用户信息保存到数据库中
     * @param
     * @return 成功返回1，失败返回-1
     */
    void save(SysUser sysUser);

    /**
     * 根据authorId查出author，并转换成它的Vo对象
     * @param authorId 这里的author指的是谁评论的
     * @return
     */
    LoginUserVo userToLoginUserVo(Long authorId);
}
