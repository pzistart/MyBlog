package com.pzi.blog.controller;

import com.pzi.blog.dao.Tag;
import com.pzi.blog.dao.mapper.TagMapper;
import com.pzi.blog.service.TagService;
import com.pzi.blog.vo.Result;
import com.pzi.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.accessibility.AccessibleStateSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pzi
 * @create 2022-03-08 18:42
 */
@RequestMapping("/tags")
@RestController
public class TagsController {

    @Autowired
    private TagService tagService;

    /**
     * 查询出所有标签  List<Tag> ==> List<TagVo>
     *
     * @return
     */
    @RequestMapping
    public Result allTags() {
        return tagService.getTagVoList();
    }

    /**
     * 根据tagId查询出标签的详细信息
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public Result TagDetailById(@PathVariable String id) {
        return tagService.tagDetailById(id);
    }

}


