package com.pzi.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzi.blog.dao.ArticleBody;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Pzi
 * @create 2022-03-06 20:41
 */
@Mapper
public interface ArticleBodyMapper extends BaseMapper<ArticleBody> {
}
