package com.pzi.blog.dao;

import lombok.Data;

@Data
public class Category {

    private Long id;

    private String avatar;

    private String categoryName;

    private String description;
}
