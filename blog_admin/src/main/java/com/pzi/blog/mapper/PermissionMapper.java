package com.pzi.blog.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzi.blog.dao.Permission;
import org.springframework.stereotype.Repository;

/**
 * @author Pzi
 * @create 2022-03-10 16:30
 */
@Repository
@InterceptorIgnore(tenantLine = "true")
public interface PermissionMapper extends BaseMapper<Permission> {
}
