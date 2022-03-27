package com.pzi.blog.service;

import com.pzi.blog.dao.Permission;
import com.pzi.blog.dao.params.PaginationParam;
import com.pzi.blog.vo.Result;

/**
 * @author Pzi
 * @create 2022-03-10 16:42
 */
public interface PermissionService {
    /**
     * 查找用户权限信息并且分页
     * @param paginationParam
     * @return
     */
    Result findPage(PaginationParam paginationParam);

    /**
     * 添加权限信息
     * @param permission
     * @return
     */
    Result addPermission(Permission permission);

    /**
     * 根据Id删除删除权限信息
     * @param id
     * @return
     */
    Result deleteById(Long id);

    /**
     * 修改权限信息
     * @param permission
     * @return
     */
    Result updatePermission(Permission permission);
}
