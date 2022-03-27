package com.pzi.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzi.blog.dao.Category;
import com.pzi.blog.vo.Result;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Pzi
 * @create 2022-03-06 20:47
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
