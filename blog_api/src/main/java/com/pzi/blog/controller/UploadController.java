package com.pzi.blog.controller;

import com.pzi.blog.utils.QiniuUtils;
import com.pzi.blog.vo.ErrorCode;
import com.pzi.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author Pzi
 * @create 2022-03-09 10:05
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping
    public Result uploadPicture(@RequestParam("image") MultipartFile file){

        String fileName = UUID.randomUUID().toString() + "."
                + StringUtils.substringAfterLast(file.getOriginalFilename(),".");

        boolean upload = qiniuUtils.upload(file, fileName);
        log.info("=========================");
        log.info("上传的结果是：" + upload);
        log.info("=========================");
        if (upload){
            return Result.success(qiniuUtils.url + fileName);
        }
        return Result.fail(20001,"上传失败");
    }

}
