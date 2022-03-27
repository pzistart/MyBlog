package com.pzi.blog.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Pzi
 * @create 2022-03-10 16:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    private Long id;

    private String username;

    private String password;

}
