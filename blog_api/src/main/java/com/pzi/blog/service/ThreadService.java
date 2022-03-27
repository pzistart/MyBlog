package com.pzi.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pzi.blog.dao.Article;
import com.pzi.blog.dao.mapper.AritcleMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Pzi
 * @create 2022-03-07 19:55
 */
@Service
public class ThreadService {

//    启动一个新的线程，来进行更新阅读数量的操作
    @Async("taskExecutor")
    public void updateViewCount(AritcleMapper aritcleMapper, Article article){

        int viewCounts = article.getViewCounts();
        Article updateArticle = new Article();
        updateArticle.setViewCounts(viewCounts + 1);

        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Article::getId,article.getId());
        lqw.eq(Article::getViewCounts,article.getViewCounts());
        aritcleMapper.update(updateArticle,lqw);

        try {
            System.out.println(Thread.currentThread().getName()+"睡眠...");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
