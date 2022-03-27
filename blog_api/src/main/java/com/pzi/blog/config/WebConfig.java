package com.pzi.blog.config;

import com.pzi.blog.handler.LoginInterceptor;
import com.pzi.blog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Pzi
 * @create 2022-02-28 15:04
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置，不可设置为*，不安全, 前后端分离项目，可能域名不一致
        //本地测试 端口不一致 也算跨域
        /*registry.addMapping("/**")
//                .allowedOrigins("http://localhost:8080","120.76.40.88:80")
                .allowedOrigins("120.76.40.88")
                .allowedMethods("POST","GET","PUT","OPTIONS","DELETE")
                .maxAge(3600);
*/
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080","http://120.76.40.88/");
//                .allowedOrigins("http://120.76.40.88/");


//                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
/*                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");*/
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        测试：访问/test路径时就会被拦截
        registry.addInterceptor(loginInterceptor).addPathPatterns("/test")
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/articles/publish");
    }

}
