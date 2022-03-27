package com.pzi.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pzi.blog.BlogApp;
import com.pzi.blog.dao.Article;
import com.pzi.blog.dao.Comment;
import com.pzi.blog.dao.SysUser;
import com.pzi.blog.dao.Tag;
import com.pzi.blog.dao.mapper.CommentMapper;
import com.pzi.blog.dao.mapper.SysUserMapper;
import com.pzi.blog.dao.mapper.TagMapper;
import com.pzi.blog.service.ArticleService;
import com.pzi.blog.service.impl.ArticleServiceImpl;
import com.pzi.blog.vo.ArticleVo;
import com.pzi.blog.vo.Result;
import net.bytebuddy.asm.Advice;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sun.tools.jar.resources.jar;

import javax.swing.text.ParagraphView;
import java.util.List;

/**
 * @author Pzi
 * @create 2022-02-28 18:34
 */
@SpringBootTest(classes = BlogApp.class)
public class ArticleServiceTest {

    @Autowired
    private ArticleServiceImpl articleService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private TagMapper tagMapper;


    @Test
    void testSelectList(){
        List<Article> articles = articleService.selectAll();
        System.out.println(articles);
    }

    @Test
    void testLongToString(){
        String date = new DateTime(46122584).toString("yyyy-MM-dd HH:mm");
        System.out.println(date);
    }


/*    @Test
    void testArToVo(){

        Article article = new Article(new Long(1),"springboot介绍以及入门案例","通过Spring Boot实现的服务，只需要依靠一个Java类，把它打包成jar，并通过`java -jar`命令就可以运行起来。\r\n\r\n这一切相较于传统Spring应用来说，已经变得非常的轻便、简单。",
                1,125,new Long(1),new Long(1),new Long(2),0,1621947720727L);

        ArticleVo articleVo = copyToVo(article);
        System.out.println(articleVo);
//        Article a = new Article()

    }*/

    //    将一个article转换成articleVo类型
    private ArticleVo copyToVo(Article article){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);

//      该属性的类型不同，所以不能复制过来，要手动复制
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        System.out.println("方法2被调用");
        return articleVo;
    }

/*    @Test
    void testGetALLTags(){
        List<Tag> allTags = tagMapper.getAllTags((long) 1);
        System.out.println(allTags);

    }*/

    @Test
    void testGetHot(){
        List<Tag> hotTags = tagMapper.getHotTags(2);
        System.out.println(hotTags);
    }

    @Test
    void getNewArticles(){

    }

    @Test
    void testTransfer(){
        String s = new DateTime(1523947720727l).toString("yyyy-MM-dd HH:mm");
        System.out.println(s);
    }

    @Test
    void testStringIsEmpty(){
        String s1 = null;
        String s2 = "   ";
//        System.out.println(s1.isEmpty());
        System.out.println(s2.isEmpty());
    }

    @Test
    void toJson(){
        SysUser sysUser = new SysUser();
        sysUser.setAccount("12");
        sysUser.setCreateDate(213123l);
        sysUser.setAdmin(122132);
        String s = JSON.toJSONString(sysUser);
        System.out.println(s);
    }


    /*    public List<LoginUserVo> getHotTags(int count) {
        List<Tag> hotTags = tagMapper.getHotTags(count);
        List<LoginUserVo> hotTagsVo = copyListTagVo(hotTags);
        return hotTagsVo;*/


    @Test
    void testHotTags(){
        List<Tag> hotTags = tagMapper.getHotTags(2);
        System.out.println(hotTags);
    }

    @Autowired
    private CommentMapper commentMapper;

    @Test
    void testSelectList1(){
        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Comment::getArticleId,1);
        List<Comment> comments = commentMapper.selectList(lqw);
        System.out.println(comments);
    }

/*    @Test
    void testJsonString(){

        Tag tag = new Tag(12123312l,"avatar","tagName");
        System.out.println(tag);
        String jsonString = JSON.toJSONString(tag);
        System.out.println(jsonString);
        Tag tag1 = JSON.parseObject(jsonString, Tag.class);
//        System.out.println();
        System.out.println("tag1的类名是："+tag1.getClass());
        System.out.println(tag1);

    }*/

    @Test
    void testStringUtil(){
        String s1 = "";
        String s2 = null;

        boolean blank = StringUtils.isBlank(s1);
        boolean blank1 = StringUtils.isBlank(s2);
        System.out.println(blank);
        System.out.println(blank1);
    }

}
