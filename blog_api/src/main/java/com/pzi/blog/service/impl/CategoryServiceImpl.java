package com.pzi.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pzi.blog.dao.Category;
import com.pzi.blog.dao.mapper.CategoryMapper;
import com.pzi.blog.service.CategoryService;
import com.pzi.blog.vo.Result;
import org.apache.commons.collections.ResettableListIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Pzi
 * @create 2022-03-06 20:45
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryMapper.selectById(categoryId);
    }

    @Override
    public Result getAllDetail() {
        List<Category> categories = categoryMapper.selectList(null);
        return Result.success(categories);
    }

    @Override
    public Result categoryDetailById(int id) {
        Category category = categoryMapper.selectById(id);
        return Result.success(category);
    }


}
