package com.pzi.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pzi.blog.dao.SysUser;
import lombok.Data;

import java.util.List;

/**
 * @author Pzi
 * @create 2022-03-08 10:14
 */
@Data
public class CommentVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private LoginUserVo author;

    private String content;

    private List<CommentVo> childrens;

    private String createDate;

    private Integer level;

    private LoginUserVo toUser;
}
