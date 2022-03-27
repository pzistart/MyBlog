package com.pzi.blog.vo.params;

import com.pzi.blog.vo.CategoryVo;
import com.pzi.blog.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private String title;

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

}
