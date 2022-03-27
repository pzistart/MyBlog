package com.pzi.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzi.blog.dao.Article_tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Pzi
 * @create 2022-03-08 19:26
 */
@Mapper
public interface Article_tagMapper extends BaseMapper<Article_tag> {
}
