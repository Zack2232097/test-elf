package com.sky.controller.discuss;

import com.sky.dto.AnswerDTO;
import com.sky.dto.QuestionDTO;
import com.sky.dto.TesterDTO;
import com.sky.result.Result;
import com.sky.service.DiscussService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//用于用户登录相关的操作
@RestController
@RequestMapping("/discuss")
@Slf4j
public class SubmitController {
    @Autowired
    private DiscussService discussService;



    //这里递交问题就行了， 后续查询相关问题和答案再思考
    @PostMapping("/submitQuestion")
    @ApiOperation("递交问题")
    public Result<String> save(@RequestBody QuestionDTO questionDTO) {
        log.info("递交问题: {}", questionDTO);
        discussService.submitQuestion(questionDTO);
        return Result.success();
    }

    //递交问题的答案
    @PostMapping("/submitAnswer")
    @ApiOperation("递交问题的答案")
    public Result<String> save(@RequestBody AnswerDTO answerDTO) {
        log.info("递交问题的答案: {}", answerDTO);
        discussService.submitAnswer(answerDTO);
        return Result.success();
    }

}
