package com.pzi.blog.controller;

import com.pzi.blog.commmon.aop.Cache;
import com.pzi.blog.commmon.aop.LogAnnotation;
import com.pzi.blog.dao.Article;
import com.pzi.blog.dao.mapper.AritcleMapper;
import com.pzi.blog.service.ArticleService;
import com.pzi.blog.vo.Result;
import com.pzi.blog.vo.params.ArticleParam;
import com.pzi.blog.vo.params.PageParams;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Pzi
 * @create 2022-02-28 16:24
 */
@Slf4j
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 查询文章并进行分页，调用业务层的方法查询好并且返回值类型是Request类型。
     *
     * @param pageParams
     * @return 返回查出来的文章结果
     */
    @PostMapping
    @Cache(name = "listArticle")
    @LogAnnotation(module = "文章",operation = "获取文章列表")
    public Result articles(@RequestBody PageParams pageParams) {
        // 业务层调用查找所有文章的方法
//        ***发现问题，没有转换成Vo类，所以前端读取不到！ ==> 已解决，是业务层的返回值写错了

        log.info("***************************");
        System.out.println("**********************");
        long time = 1647793168622l;
//        这个方法在转换成日期格式的时候就错了，就少了8个小时
        String timeStr = new DateTime(time, DateTimeZone.forOffsetHours(8)).toString("yyyy-MM-dd HH:mm");
        System.out.println(timeStr);
        log.info("***************************");
        System.out.println("**********************");

/*        log.info("*************format**************");
        System.out.println("**********************");
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String format = simpleDateFormat.format(date);
        System.out.println(format);
        log.info("*************format**************");
        System.out.println("**********************");*/

        return articleService.listArticlePageXml(pageParams);
//        return articleService.listArticlePage(pageParams);
    }

//    @Cache(name = "最热文章")
    @PostMapping("/hot")
    public Result hotArticle(){
        return articleService.getHotArticle(5);
    }

//    @Cache(name = "最新文章")
    @PostMapping("/new")
    public Result newArticles(){
        return articleService.getNewArticles(3);
    }

    @PostMapping("/listArchives")
    public Result listArchives(){
        return articleService.listArchives(100);
    }

    /**
     * 查看文章详情，阅读文章
     * @param articleId
     * @return
     */
    @PostMapping("/view/{id}")
    public Result view(@PathVariable("id")String articleId){
        Result articleById = articleService.findArticleById(articleId);
        return articleById;
    }

    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        Result result = articleService.insertArticle(articleParam);
        log.info("title是："+ articleParam.getTitle());
        return result;
    }

}
