package com.pzi.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzi.blog.dao.Article;
import com.pzi.blog.dao.dos.Archives;
import com.pzi.blog.vo.params.PageParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import sun.util.resources.cldr.gv.LocaleNames_gv;

import java.util.List;

/**
 * @author Pzi
 * @create 2022-02-28 16:25
 */
@Mapper
//这里的泛型就是用来给数据库查找时用的，select xx from ms_article
public interface AritcleMapper extends BaseMapper<Article> {

//    文章归档，归类同年同月下的文章总数
    List<Archives> listArchiver(int count);

    List<Article> listArticleByTagId(Long tagId);

    List<Article> listArticlePageXml(@Param("page") int page,
                                     @Param("pageSize")int pageSize,
                                     @Param("categoryId") Long categoryId,
                                     @Param("tagId") Long tagId,
                                     @Param("year") String year,
                                     @Param("month") String month);
}
