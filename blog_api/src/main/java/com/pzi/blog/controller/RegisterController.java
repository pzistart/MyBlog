package com.pzi.blog.controller;

import com.pzi.blog.service.LoginService;
import com.pzi.blog.vo.Result;
import com.pzi.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.rmi.runtime.Log;

/**
 * @author Pzi
 * @create 2022-03-06 15:13
 */
@RequestMapping("/register")
@RestController
public class RegisterController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result register(@RequestBody LoginParams params){
        return loginService.register(params);
    }

}
