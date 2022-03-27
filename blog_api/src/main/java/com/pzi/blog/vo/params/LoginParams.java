package com.pzi.blog.vo.params;

import lombok.Data;

/**
 * @author Pzi
 * @create 2022-03-05 14:05
 */
@Data
public class LoginParams {
    private String account;
    private String password;
    private String nickname;
}
