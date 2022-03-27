package com.pzi.blog.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Pzi
 * @create 2022-03-08 19:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article_tag {

    private String id;

    private String articleId;

    private String tagId;
}
