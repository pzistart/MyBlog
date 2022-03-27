package com.pzi.blog.controller;

import com.pzi.blog.service.TagService;
import com.pzi.blog.vo.TagVo;
import com.pzi.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Pzi
 * @create 2022-03-04 16:45
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/hot")
    public Result hotTags(){
        List<TagVo> hotTags = tagService.getHotTags(2);
        return Result.success(hotTags);
    }

    @GetMapping("/detail")
    public Result getAllDetail(){
        return tagService.getAllTagsDetail();
    }

}
