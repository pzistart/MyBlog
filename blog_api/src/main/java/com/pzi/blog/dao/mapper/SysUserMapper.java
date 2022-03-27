package com.pzi.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzi.blog.dao.SysUser;
import com.pzi.blog.vo.params.LoginParams;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Pzi
 * @create 2022-03-01 21:01
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {


}
