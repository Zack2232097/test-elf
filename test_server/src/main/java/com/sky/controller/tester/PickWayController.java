package com.sky.controller.tester;

import com.sky.result.Result;
import com.sky.service.LogService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//用户上传日志后可以选择,日志分析,性能统计等个性化服务,所上传后不立即处理
@Slf4j
@RestController
@RequestMapping("/tester")
public class PickWayController {
    @Autowired
    private LogService logService;


    @GetMapping("/analyze")
    @ApiOperation("根据logID日志分析")
    public Result<String> analyze(Long id) {
        log.info("处理的logID为: {}", id);
        logService.logAnalysis(id);
        return Result.success();
    }
}
