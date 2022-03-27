package com.pzi.blog.controller;
import com.pzi.blog.service.TokenService;
import com.pzi.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pzi
 * @create 2022-03-05 16:21
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private TokenService tokenService;

    /**
     * 调用tokenService进行校验，如果校验通过则返回token中的信息给前端
     * @param
     * @return
     */
    @GetMapping("/currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
        System.out.println("token接受成功");
        System.out.println(token);
        return tokenService.getUserByToken(token);
    }

}

