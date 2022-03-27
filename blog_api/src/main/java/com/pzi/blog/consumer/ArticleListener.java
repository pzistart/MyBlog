package com.pzi.blog.consumer;

import com.alibaba.fastjson.JSON;
import com.pzi.blog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;

/**
 * @author Pzi
 * @create 2022-03-23 14:47
 */
@Slf4j
@Component
public class ArticleListener {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ArticleService articleService;

    @RabbitListener(queues = "article.queue")
    public void listenArticleInsert(String msg){
        System.out.println("消费者接收到删除缓存的消息：【" + msg + "】");
//        listArticle::ArticleController::articles::83dab9ea501defddc22656c0e4f2d80f
//        String redisKey = "所有文章::ArticleController::articles::";
//        long expire = 60000;

//        新加入文章之后，直接删除文章相关的所有缓存
//        模糊查询，找到以listArticle相关的keys
        Set<String> keys = redisTemplate.keys("listArticle*");
        keys.forEach(s -> {
            redisTemplate.delete(s);
            log.info("删除了文章列表的缓存：{}",s);
        });

    }

}
