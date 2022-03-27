package com.pzi.blog.controller;

import com.pzi.blog.dao.Category;
import com.pzi.blog.dao.mapper.CategoryMapper;
import com.pzi.blog.service.CategoryService;
import com.pzi.blog.vo.CategoryVo;
import com.pzi.blog.vo.Result;
import com.sun.mail.imap.protocol.ID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pzi
 * @create 2022-03-08 16:45
 */
@RestController
@RequestMapping("/categorys")
public class CategorysController {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping
    public Result categorys(){
        List<Category> categories = categoryMapper.selectList(null);
        List<CategoryVo> categoryVoList = copyList(categories);
        return Result.success(categoryVoList);
    }

    private List<CategoryVo> copyList(List<Category> categories) {
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categories){
            CategoryVo categoryVo = copyToVo(category);
            categoryVoList.add(categoryVo);
        }
        return categoryVoList;
    }

    private CategoryVo copyToVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    @GetMapping("/detail")
    public Result detail(){
        return categoryService.getAllDetail();
    }

    @GetMapping("/detail/{id}")
    public Result categoryDetailById(@PathVariable int id){
        return categoryService.categoryDetailById(id);
    }

}
