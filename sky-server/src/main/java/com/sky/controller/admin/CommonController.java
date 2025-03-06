package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {

    @Autowired
    private  AliOssUtil aliOssUtil;

    public CommonController(AliOssUtil aliOssUtil) {
        this.aliOssUtil = aliOssUtil;
    }

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    //抛出的异常会被handler处理
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("文件上传, 文件名: {}", file.getOriginalFilename());
        //调用阿里云OSS工具类进行文件上传
        String url = aliOssUtil.upload(file);
        log.info("文件上传完成,文件访问的url: {}", url);
        return Result.success(url);
    }
}
