package com.sky.controller.tester;

import com.sky.result.Result;
import com.sky.service.LogService;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//用户上传日志后可以选择,日志分析,性能统计,等个性化服务,所以我们不上传就直接处理
//况且文件比较大
@Slf4j
@RestController
@RequestMapping("/tester")
public class PickWayController {

    @Autowired
    private  AliOssUtil aliOssUtil;
    @Autowired
    private LogService logService;


    @GetMapping("/analyze")
    @ApiOperation("根据logID日志分析")
    //抛出的异常会被handler处理
    public Result<String> analyze(Long id) {
        log.info("处理的logID为: {}", id);
        logService.logAnalysis(id);
        return Result.success();
    }
}
