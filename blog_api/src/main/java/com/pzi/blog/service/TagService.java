package com.pzi.blog.service;

import com.pzi.blog.vo.Result;
import com.pzi.blog.vo.TagVo;

import java.util.List;

/**
 * @author Pzi
 * @create 2022-03-04 15:52
 */
public interface TagService {

    List<TagVo> getAllTags(String id);

    List<TagVo> getHotTags(int count);

    /**
     * 查询出所有标签的详细信息
     * @return
     */
    Result getAllTagsDetail();

    Result getTagVoList();

    Result tagDetailById(String id);
}
