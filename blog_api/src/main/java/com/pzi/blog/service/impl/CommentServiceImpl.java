package com.pzi.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pzi.blog.dao.Comment;
import com.pzi.blog.dao.SysUser;
import com.pzi.blog.dao.mapper.CommentMapper;
import com.pzi.blog.service.CommentService;
import com.pzi.blog.service.UserService;
import com.pzi.blog.utils.UserThreadLocal;
import com.pzi.blog.vo.CommentVo;
import com.pzi.blog.vo.Result;
import com.pzi.blog.vo.params.CommentParam;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.chrono.IsoChronology;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pzi
 * @create 2022-03-08 10:07
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserService userService;

//    问题：有两条评论A,B;B是A的子评论，但是显示所有评论的时候，{B会在父评论的位置多显示一次}此情况不符合实际
//    加入判断，如果level是2，就不加入到 List<CommentVo> 中，那么回显数据的时候不会出现该情况

    @Override
    public Result getCommentsById(Long articleId) {
//        通过文章id查出该文章的所有评论
        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Comment::getArticleId, articleId);
        List<Comment> comments = commentMapper.selectList(lqw);

//        把查出来的List<Comment>，转换成List<CommentVo>的形式
        List<CommentVo> commentVos = copyList(comments,true);
//        把每一个封装好的CommentVo返回的时候，由于数据库采用的id生成策略是雪花算法，那么在将数据返回给前端时
//        前端读取数据会出现精度错误，所以把Long型的数字序列化成Json字符串的方式返回给前端读取
//        否则的话前端读数就读错了，那么在接下来的评论的子评论中，读取评论id也会出错，因为是从前端读取的请求参数
        return Result.success(commentVos);
    }

    @Override
    public List<CommentVo> findChildrens(Long parentId) {
        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Comment::getParentId, parentId);
        lqw.eq(Comment::getLevel, 2);
        List<Comment> comments = commentMapper.selectList(lqw);
//        再将List<Comment> ==> List<CommentVo>
        List<CommentVo> commentVos = copyList(comments);
        return commentVos;
    }

    @Override
    public Result insertComment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Long authorId = sysUser.getId();

//        得到前端传过来的文章id
        Long articleId = commentParam.getArticleId();
        String content = commentParam.getContent();
//
        Long parentId = commentParam.getParent();
        Long toUserId = commentParam.getToUserId();

        Comment comment = new Comment();
//      comment的id无需设置，自增型
        comment.setAuthorId(authorId);
        comment.setContent(content);
        comment.setCreateDate(System.currentTimeMillis());
        comment.setArticleId(articleId);
        comment.setParentId(parentId == null ? 0 : parentId);
        comment.setToUid(toUserId == null ? 0 : toUserId);
        if (parentId == null || parentId == 0) {
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }

        commentMapper.insert(comment);

        return Result.success(null);
    }

//    用来转换父评论List
    private List<CommentVo> copyList(List<Comment> comments,Boolean onlyParent) {

        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {

            if (comment.getLevel() != 1){
                continue;
            }

            CommentVo commentVo = copyToVo(comment);
            commentVoList.add(commentVo);
        }
        return commentVoList;
    }

//    用来转换子评论List
    private List<CommentVo> copyList(List<Comment> comments) {

        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentVo commentVo = copyToVo(comment);
            commentVoList.add(commentVo);
        }
        return commentVoList;
    }

    /**
     * 将commment ==> commentVo
     *
     * @param comment
     * @return
     */
    private CommentVo copyToVo(Comment comment) {
        CommentVo commentVo = new CommentVo();
//        将id content level 等复制过来
        BeanUtils.copyProperties(comment, commentVo);
//        根据author_id查询出SysUser ==> LoginUserVo，再set到CommentVo中
        commentVo.setAuthor(userService.userToLoginUserVo(comment.getAuthorId()));

//        将日期转换成所需要的格式，并set到CommentVo中
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

//        childrens子评论
//        判断当前comment是否有子评论？comment.id(53) == parent_id去查询
//          SELECT * FROM ms_comment c WHERE parent_id = 53;
        List<CommentVo> childrens = findChildrens(comment.getId());
//        如果当前评论有子评论，那么set到CommentVo中
        if (childrens != null) {
            commentVo.setChildrens(childrens);
        }
//        level = 2才有toUser
        if (comment.getLevel() > 1) {
            commentVo.setToUser(userService.userToLoginUserVo(comment.getAuthorId()));
        }

        return commentVo;
    }
}




