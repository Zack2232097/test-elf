package com.sky.controller.discuss;


import com.sky.result.Result;
import com.sky.service.HotQuestionService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotUpdate")
@Slf4j
public class HotController {
    @Autowired
    private HotQuestionService hotQuestionService;

    @PutMapping("/{status}")
    @ApiOperation("传参hotUpdate策略")
    public Result setStatus(@PathVariable Integer status) {
        hotQuestionService.update(status);
        return Result.success();
    }
    //class对象
    //字段还有方法
}
