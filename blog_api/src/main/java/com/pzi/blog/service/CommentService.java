package com.pzi.blog.service;

import com.pzi.blog.dao.Comment;
import com.pzi.blog.vo.CommentVo;
import com.pzi.blog.vo.Result;
import com.pzi.blog.vo.params.CommentParam;

import java.util.List;

/**
 * @author Pzi
 * @create 2022-03-08 10:07
 */
public interface CommentService {
    /**
     * 根据文章id查询文章的所有评论
     * @param articleId
     * @return
     */
    Result getCommentsById(Long articleId);

    /**
     * 查找当前评论有没有子评论
     * @param parentId
     * @return
     */
    List<CommentVo> findChildrens(Long parentId);

    /**
     * 新增评论
     * @param commentParam
     * @return
     */
    Result insertComment(CommentParam commentParam);
}
