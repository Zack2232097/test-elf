package com.sky.controller.tester;

import com.sky.dto.CasePageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CaseService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/tester")
public class CaseController {
    @Autowired
    private CaseService caseService;

    @GetMapping("/page")
    @ApiOperation("结果分页查询（单个case地）")
    public Result<PageResult> page(CasePageQueryDTO casePageQueryDTO) {
        log.info("case分页查询:{}", casePageQueryDTO);
        PageResult pageResult = caseService.pageQuery(casePageQueryDTO);
        return Result.success(pageResult);
    }
}
