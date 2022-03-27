package com.pzi.blog.controller;

import com.pzi.blog.service.CommentService;
import com.pzi.blog.vo.Result;
import com.pzi.blog.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

/**
 * @author Pzi
 * @create 2022-03-08 10:01
 */
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/article/{id}")
    public Result comments(@PathVariable("id") Long articleId){
        return commentService.getCommentsById(articleId);
    }

    @PostMapping("/create/change")
    public Result change(@RequestBody CommentParam commentParam){

        return commentService.insertComment(commentParam);
    }

}
