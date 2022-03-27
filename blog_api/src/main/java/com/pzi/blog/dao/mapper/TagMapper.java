package com.pzi.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzi.blog.dao.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Pzi
 * @create 2022-03-04 15:55
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> getAllTags(String id);

    List<Tag> getHotTags(int count);

}
