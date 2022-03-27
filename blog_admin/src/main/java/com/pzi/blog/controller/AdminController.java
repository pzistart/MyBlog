package com.pzi.blog.controller;

import com.pzi.blog.dao.Permission;
import com.pzi.blog.dao.params.PaginationParam;
import com.pzi.blog.service.PermissionService;
import com.pzi.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Pzi
 * @create 2022-03-10 16:07
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PermissionService permissionService;

    @PostMapping("/permission/permissionList")
    public Result findPage(@RequestBody PaginationParam paginationParam){
        return permissionService.findPage(paginationParam);
    }

    @PostMapping("/permission/add")
    public Result addPermission(@RequestBody Permission permission){
        return permissionService.addPermission(permission);
    }
    @GetMapping("/permission/delete/{id}")
    public Result deleteByRow(@PathVariable("id") Long id){
        return permissionService.deleteById(id);
    }

    @PostMapping("/permission/update")
    public Result updatePermission(@RequestBody Permission permission){
        return permissionService.updatePermission(permission);
    }

}
