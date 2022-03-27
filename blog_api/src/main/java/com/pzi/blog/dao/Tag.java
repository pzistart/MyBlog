package com.pzi.blog.dao;

import com.pzi.blog.commmon.aop.Cache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    private String id;

    private String avatar;

    private String tagName;

}
