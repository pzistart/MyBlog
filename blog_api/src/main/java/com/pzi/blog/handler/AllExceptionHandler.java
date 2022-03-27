package com.pzi.blog.handler;

import com.pzi.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Pzi
 * @create 2022-03-04 19:29
 */
@ControllerAdvice
public class AllExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public Result doException(Exception e){
        e.printStackTrace();
        return Result.fail(-999,"系统错误");
    }


}
