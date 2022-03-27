package com.pzi.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzi.blog.dao.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Pzi
 * @create 2022-03-08 10:05
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
