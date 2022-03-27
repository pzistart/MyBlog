package com.pzi.blog.controller;

import com.pzi.blog.service.LoginService;
import com.pzi.blog.vo.Result;
import com.pzi.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Pzi
 * @create 2022-03-05 13:59
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParams loginParams){
        Result login = loginService.login(loginParams);
        return login;
    }

}
