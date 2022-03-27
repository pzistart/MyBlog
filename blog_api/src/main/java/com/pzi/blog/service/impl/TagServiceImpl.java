package com.pzi.blog.service.impl;

import com.pzi.blog.dao.Tag;
import com.pzi.blog.dao.mapper.TagMapper;
import com.pzi.blog.service.TagService;
import com.pzi.blog.vo.Result;
import com.pzi.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pzi
 * @create 2022-03-04 15:54
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> getAllTags(String id) {
        List<Tag> allTags = tagMapper.getAllTags(id);
        List<TagVo> tagVos = copyListTagVo(allTags);
        return tagVos;
    }

    @Override
    public List<TagVo> getHotTags(int count) {
        List<Tag> hotTags = tagMapper.getHotTags(count);
        List<TagVo> tagVos = copyListTagVo(hotTags);
        return tagVos;
    }

    @Override
    public Result getAllTagsDetail() {
        List<Tag> tags = tagMapper.selectList(null);
        return Result.success(tags);
    }

    @Override
    public Result getTagVoList() {

        List<Tag> tags = tagMapper.selectList(null);
        List<TagVo> tagVos = copyListTagVo(tags);
        return Result.success(tagVos);

    }

    @Override
    public Result tagDetailById(String id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(tag);
    }

    private List<TagVo> copyListTagVo(List<Tag> allTags) {
//        循环遍历，将list中所有的tag ==> tagVo
        List<TagVo> tagVos = new ArrayList<>();
        for (Tag tag : allTags) {
            TagVo tagVo = copyToTagVo(tag);
            tagVos.add(tagVo);
        }

        return tagVos;
    }

    private TagVo copyToTagVo(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
        return tagVo;
    }

}
