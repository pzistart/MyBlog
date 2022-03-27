package com.pzi.blog.service;

import com.pzi.blog.dao.Category;
import com.pzi.blog.vo.Result;

/**
 * @author Pzi
 * @create 2022-03-06 20:45
 */
public interface CategoryService {
    Category getCategoryById(Long categoryId);

    /**
     * 查询出所有的分类
     * @return
     */
    Result getAllDetail();

    /**
     * 根据分类id查询出分类详情
     * @param id
     * @return
     */
    Result categoryDetailById(int id);
}
