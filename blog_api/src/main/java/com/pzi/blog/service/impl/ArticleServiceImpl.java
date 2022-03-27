package com.pzi.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzi.blog.commmon.aop.LogAnnotation;
import com.pzi.blog.dao.*;
import com.pzi.blog.dao.dos.Archives;
import com.pzi.blog.dao.mapper.AritcleMapper;
import com.pzi.blog.dao.mapper.ArticleBodyMapper;
import com.pzi.blog.dao.mapper.Article_tagMapper;
import com.pzi.blog.service.ArticleService;
import com.pzi.blog.service.CategoryService;
import com.pzi.blog.service.ThreadService;
import com.pzi.blog.service.TokenService;
import com.pzi.blog.utils.JWTUtils;
import com.pzi.blog.utils.UserThreadLocal;
import com.pzi.blog.vo.*;
import com.pzi.blog.vo.params.ArticleParam;
import com.pzi.blog.vo.params.PageParams;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolverSupport;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Pzi
 * @create 2022-02-28 18:11
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private AritcleMapper aritcleMapper;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TagServiceImpl tagService;

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private Article_tagMapper article_tagMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 将一个包含所有文章的List<article>，转换成List<articleVo>
     *
     * @param records
     * @return
     */
    private List<ArticleVo> copyList(List<Article> records) {
        System.out.println("方法1被调用");
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article article : records) {
            ArticleVo articleVo = copyToVo(article, true, true, false, false);
            articleVoList.add(articleVo);
        }
        return articleVoList;
    }

    /*   */

    /**
     * 将一个article转换成articleVo类型，若有作者和标签则也添加并转换
     *
     * @param article
     * @param isAuthor
     * @param isTag
     * @return
     *//*
    private ArticleVo copyToVo(Article article,Boolean isAuthor,Boolean isTag){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);

//      pojo和Vo中二者的该属性的类型不同，所以不能复制过来，要手动复制
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
//        System.out.println("方法2被调用");

//        如果有作者，调用authorService的根据文章作者id查询作者的方法
        if (isAuthor){
            Long authorId = article.getAuthorId();
//            根据作者id查询作者信息
            SysUser sysUser = userService.selectAuthor(authorId);
            if(sysUser == null){
//                如果查不到作者信息，则返回一个匿名的作者
                articleVo.setAuthor("匿名");
            }else {
                articleVo.setAuthor(sysUser.getNickname());
            }
        }

//        查询出该article对应的所有标签
        if (isTag){
            List<TagVo> allTags = tagService.getAllTags(article.getId());
            articleVo.setTags(allTags);
        }

        return articleVo;
    }*/
    private ArticleVo copyToVo(Article article, Boolean isAuthor, Boolean isTag, Boolean isArticleBody, Boolean isCategorys) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);

//      pojo和Vo中二者的该属性的类型不同，所以不能复制过来，要手动复制
//        new DateTime(time, DateTimeZone.forOffsetHours(8)).toString("yyyy-MM-dd HH:mm");
        articleVo.setCreateDate(new DateTime(article.getCreateDate(), DateTimeZone.forOffsetHours(8)).toString("yyyy-MM-dd HH:mm"));
        System.out.println("!!!!!!!!!!传给前端的createTime!!!!!!!!!!!!");
        System.out.println(article.getCreateDate());
//        时间戳 ==> data格式的时间 这个时间戳所在的地区有*问题
        System.out.println(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        System.out.println("!!!!!!!!!!传给前端的createTime!!!!!!!!!!!!");
//        System.out.println("方法2被调用");

//        如果有作者，调用authorService的根据文章作者id查询作者的方法
        if (isAuthor) {
            Long authorId = article.getAuthorId();
//            根据作者id查询作者信息
            SysUser sysUser = userService.selectAuthor(authorId);
            if (sysUser == null) {
//                如果查不到作者信息，则返回一个匿名的作者
                articleVo.setAuthor("匿名");
            } else {
                articleVo.setAuthor(sysUser.getNickname());
            }
        }

//        查询出该article对应的所有标签
        if (isTag) {
            List<TagVo> allTags = tagService.getAllTags(article.getId());
            articleVo.setTags(allTags);
        }

//        查询出id对应的articleBody，并转换从它的Vo形式
        if (isArticleBody) {
            Long bodyId = article.getBodyId();
            ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
            ArticleBodyVo articleBodyVo = new ArticleBodyVo();
            articleBodyVo.setContent(articleBody.getContent());
            articleVo.setBody(articleBodyVo);
        }

//        查询出id对应的List<categorys>，并转换从它的Vo形式
        if (isCategorys) {
            Long categoryId = article.getCategoryId();
            Category category = categoryService.getCategoryById(categoryId);
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category, categoryVo);
            articleVo.setCategory(categoryVo);
        }
        return articleVo;
    }


    //    由于查看文章归档的时候需要根据日期年月归档，而数据库中存储的是时间戳的形式，mbp的拼接无法将时间戳转换成年和月的形式
//    而xml的查询语句可以支持转换。转换好年月的形式后，再匹配请求参数中的年月，就可得到正确的数据
    @Override
    public Result listArticlePageXml(PageParams pageParams) {

//        这里虽然设置了参数，不过也穿不过去的。因为是需要请求参数
        int page = pageParams.getPage();
        page = (page-1)* pageParams.getPageSize();
//                page <= 1 ? 0 : (page-1)*pageSize

        System.out.println("!!!!!BEGIN!!!!!!");
//        查询所有 Article
        List<Article> articles = aritcleMapper.listArticlePageXml(
                page,
                pageParams.getPageSize(),
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth()
                );
        System.out.println("!!!!!!END!!!!!");

//        将Article ==> ArticleVo
        List<ArticleVo> articleVoList = copyList(articles);
        System.out.println("********包装Vo后********");
//        System.out.println(articleVoList.get(0));
        Result result = Result.success(articleVoList);


//        获取data中List<?>里面的?类型
        Class clazz = result.getClass();
        try {
            Field listField = clazz.getDeclaredField("data");
            Type genericType = listField.getGenericType();
            System.out.println("包装data的类型是"+genericType);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        System.out.println(result);
        System.out.println("********包装Vo后********");
        return result;
    }


    /**
     * 分页查询文章
     *
     * @param pageParams
     * @return
     */
    @Override
    public Result listArticlePage(PageParams pageParams) {

        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper();

//        如果请求参数中带有目录id，则也将它加入作为条件 查询文章列表
        if (pageParams.getCategoryId() != null) {
            lqw.eq(Article::getCategoryId, pageParams.getCategoryId());
        }

        //        如果请求参数中带有标签tagId，则也将它加入作为条件 查询文章列表
        if (pageParams.getTagId() != null) {
//            从article-tag联合表中查出tag对应的所有文章
            LambdaQueryWrapper<Article_tag> qw = new LambdaQueryWrapper<>();
            qw.eq(Article_tag::getTagId, pageParams.getTagId());
            qw.select(Article_tag::getArticleId);

//            从查询出的list获取文章id
            List<Article_tag> list = article_tagMapper.selectList(qw);
            System.out.println(list);

            if (list.size() > 0) {
                List<String> articleIds = new ArrayList<>();
                for (Article_tag article_tag : list){
                    articleIds.add(article_tag.getArticleId());
                }
                System.out.println("!!!!!!!!!!!!!!");
                System.out.println(articleIds);
                System.out.println("!!!!!!!!!!!!!!");

                lqw.in(Article::getId,articleIds);
            }
        }


        String year = pageParams.getYear();
        String month = pageParams.getMonth();

        if (year != null && month !=null){

        }

//        按置顶和按时间从近到远排序
        lqw.orderByDesc(Article::getWeight, Article::getCreateDate);
//        分页完之后，拿到数据records
        aritcleMapper.selectPage(page, lqw);
        List<Article> records = page.getRecords();
//        不能直接将records放到Result的data中去，因为格式与前端所需要的不符，下面进行转换
        List<ArticleVo> articleVoList = copyList(records);
//        这里返回的data没改！刚刚用的仍然是转换Vo之前的！
        return Result.success(articleVoList);
    }


    /**
     * 获取前 count条 最热文章
     *
     * @param count
     * @return
     */

    @Override
    public Result getHotArticle(int count) {
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.select(Article::getId, Article::getTitle);
        lqw.orderByDesc(Article::getViewCounts);
        lqw.last("limit " + count);
//        List<Article> articles = aritcleMapper.selectList(lqw);
//        return Result.success(copyList(articles));
        List<Article> articles = aritcleMapper.selectList(lqw);
        return Result.success(copyList(articles));
//SELECT a.title
//  FROM ms_article a
//ORDER BY a.view_counts DESC
//LIMIT 3;
    }

    /**
     * 获取前 count条 最新的文章
     *
     * @param count
     * @return
     */
    @Override
    public Result getNewArticles(int count) {
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.select(Article::getId, Article::getTitle);
        lqw.orderByDesc(Article::getCreateDate);
        lqw.last("limit " + count);
        List<Article> articles = aritcleMapper.selectList(lqw);
        return Result.success(copyList(articles));
//        SELECT a.title
        //  FROM ms_article a
        //ORDER BY a.create_date DESC
        //LIMIT 2
    }

    @Override
    public Result listArchives(int i) {
        List<Archives> archives = aritcleMapper.listArchiver(i);
        return Result.success(archives);
    }

    @Override
    public Result findArticleById(String articleId) {

//        1、先根据articleId查出article
//        2、再把article转换成articleVo并且set值{articleBody,category}
        Article article = aritcleMapper.selectById(articleId);
        ArticleVo articleVo = copyToVo(article, true, true, true, true);
//        查看完文章之后，同时增加一次阅读次数。但是这个操作不要放在主线程中执行。
        threadService.updateViewCount(aritcleMapper, article);
        return Result.success(articleVo);
    }

    @Override
    public Result insertArticle(ArticleParam articleParam) {

        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();

        String title = articleParam.getTitle();
        String content = articleParam.getBody().getContent();//文章内容
        String contentHtml = articleParam.getBody().getContentHtml();//内容html
        CategoryVo category = articleParam.getCategory();
        String summary = articleParam.getSummary();//文章概叙
        List<TagVo> tags = articleParam.getTags();//文章的所有标签

        Article article = new Article();
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setSummary(summary);
        article.setTitle(title);
        article.setViewCounts(0);
        article.setWeight(0);
        article.setCategoryId(category.getId());
        article.setAuthorId(userId);
        aritcleMapper.insert(article);

//        插入数据库后，mp会自动生成文章id
        String id = article.getId();

        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(content);
        articleBody.setContentHtml(contentHtml);
        articleBody.setArticleId(id);
        articleBodyMapper.insert(articleBody);
//        插入数据库后，mp会自动生成文章body的id
        Long bodyId = articleBody.getId();

        article.setBodyId(bodyId);
//        更新article，为它赋值bodyId
        aritcleMapper.updateById(article);

//        如果前端传递过来的tags不为空，那么则为文章和标签建立关联
        if (tags != null) {
            List<Article_tag> list = new ArrayList<>();
            for (TagVo tagVo : tags) {
                Article_tag article_tag = new Article_tag();
                article_tag.setArticleId(id);
                article_tag.setTagId(tagVo.getId());
                list.add(article_tag);
            }
            for (Article_tag article_tag : list) {
                article_tagMapper.insert(article_tag);
            }
        }


        Map<String, String> data = new HashMap<>();
        data.put("id", id.toString());

//        新增文章后，通知消费者更新缓存
        String queue = "article.queue";
        String msg = "通知你更新缓存";
        rabbitTemplate.convertAndSend(queue,msg);

        return Result.success(data);
    }

    //    查询所有的文章，返回一个list
    public List<Article> selectAll() {
        List<Article> articles = aritcleMapper.selectList(null);
        return articles;
    }

}

