package com.pzi.blog.service;

import com.pzi.blog.vo.Result;
import com.pzi.blog.vo.params.ArticleParam;
import com.pzi.blog.vo.params.PageParams;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Pzi
 * @create 2022-02-28 18:09
 */

public interface ArticleService {

    /**
     * 根据请求中的分页参数进行条件查询
     * @param pageParams
     * @return
     */
    Result listArticlePage(PageParams pageParams);

    Result getHotArticle(int count);

    Result getNewArticles(int count);

    Result listArchives(int i);

    /**
     * 通过文章id查询文章的所有具体信息
     * @param articleId
     * @return
     */
    Result findArticleById(String articleId);

    /**
     * 向数据库中加入文章
     * @param articleParam
     * @return
     */
    @Transactional
    Result insertArticle(ArticleParam articleParam);

    Result listArticlePageXml(PageParams pageParams);
}
