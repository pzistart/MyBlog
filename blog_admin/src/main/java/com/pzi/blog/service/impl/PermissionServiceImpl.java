package com.pzi.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzi.blog.dao.Permission;
import com.pzi.blog.dao.params.PaginationParam;
import com.pzi.blog.mapper.PermissionMapper;
import com.pzi.blog.service.PermissionService;
import com.pzi.blog.vo.PaginationVo;
import com.pzi.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Pzi
 * @create 2022-03-10 16:42
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Result findPage(PaginationParam paginationParam) {
        Page<Permission> page = new Page<>(paginationParam.getCurrentPage(), paginationParam.getPageSize());
        LambdaQueryWrapper<Permission> lqw = new LambdaQueryWrapper<>();

        if (paginationParam.getQueryString() != null) {
            lqw.like(Permission::getName,paginationParam.getQueryString());
//            lqw.last("where name LIKE %" + paginationParam.getQueryString() + "%" );
        }
        permissionMapper.selectPage(page, lqw);
        System.out.println("Page的size = " + page.getSize());

//      如果 total % pageSize = 0，那么回到 (currentPage-1) 的那一页
/*
        long total = page.getTotal();
        long size = page.getSize();
        long maxShowPage = 0;
        if (total % size == 0){
            maxShowPage = page.getCurrent() - 1;
        }else {
            maxShowPage = page.getCurrent();
        }

        System.out.println("Page的total = " + total);
*/

        PaginationVo paginationVo = new PaginationVo();
        paginationVo.setList(page.getRecords());
        paginationVo.setTotal(page.getTotal());

        return Result.success(paginationVo);
    }

    @Override
    public Result addPermission(Permission permission) {
        return Result.success(permissionMapper.insert(permission));
    }

    @Override
    public Result deleteById(Long id) {
        return Result.success(permissionMapper.deleteById(id));
    }

    @Override
    public Result updatePermission(Permission permission) {
        return Result.success(permissionMapper.updateById(permission));
    }



}
