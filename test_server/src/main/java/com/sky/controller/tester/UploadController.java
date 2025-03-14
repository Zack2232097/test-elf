package com.sky.controller.tester;

import com.sky.result.Result;
import com.sky.service.LogService;
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
@RequestMapping("/tester")
public class UploadController {

    @Autowired
    private  AliOssUtil aliOssUtil;
    @Autowired
    private LogService logService;


    @PostMapping("/upload")
    @ApiOperation("文件上传")
    //抛出的异常会被handler处理
    public Result<Long> upload(MultipartFile file) throws IOException {
        log.info("文件上传, 文件名: {}", file.getOriginalFilename());
        //调用阿里云OSS工具类进行文件上传
        //尤其是压测的日志, 大小可能会超过一个G, 用户有这个需求不愿意电脑上保存这么大的文件
        String url = aliOssUtil.upload(file);
        log.info("文件上传完成,文件访问的url: {}", url);
        Long logID = logService.save(url);

        // 前端需要logid:
        // 日志上传后,然后(上传logid)点击pickway中的日志分析, 后面可以分页查询,都是要基于logid的
        return Result.success(logID);
    }
}
