package com.sky.controller.discuss;

import com.sky.dto.QuestionDTO;
import com.sky.entity.Answer;
import com.sky.entity.Question;
import com.sky.result.Result;
import com.sky.service.DiscussService;
import com.sky.service.HotQuestionService;
import com.sky.vo.HotQuestionView;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//用于用户登录相关的操作
@RestController
@RequestMapping("/discuss")
@Slf4j
public class ViewController {
    @Autowired
    private DiscussService discussService;

    //这里递交问题就行了， 后续查询相关问题和答案再思考
    @GetMapping("/viewQuestion")
    @ApiOperation("查看讨论区的问题")
    public Result<List<Question>> viewQuestion() {
        log.info("查看讨论区的问题...");
        //这里不必分页了
        //由于是直接从数据库查出来的, 所以使用Question.
        List<Question> list = discussService.viewQuestion();
        return Result.success(list);
    }

    // 查看问题的答案(需要问题的ids),需要RequestParam将前端上传的1,2,3解析为List<Integer>
    // 除非viewAnswer的参数是一个, Long id, 则不需要RequestParam
    // GET 方法不支持 @RequestBody，因为 GET 是 无请求体（body）的 HTTP 方法，所有参数 必须在 URL 里传递。
    @GetMapping("/viewAnswer")
    @ApiOperation("查看问题的答案")
    public Result<List<Answer>> viewAnswer(@RequestParam List<Integer> question_ids) {
        log.info("查看问题的答案...");
        List<Answer> list = discussService.viewAnswerbyQuestionId(question_ids);
        return Result.success(list);
    }

    @RestController
    public class HotQuestionController {
        @Autowired
        private HotQuestionService hotQuestionService;

        // 获取热点问题和答案
        @GetMapping("/view")
        public List<HotQuestionView> getHotQuestions() {
            return hotQuestionService.getHotQuestions();
        }

        //查询问题时,会将问题的question_id返回给前端, 如果要查看该问题的相关问题, 再提交该问题question_id
        // 根据question_id查找相关问题(返回相关问题的ids(后面改为问题加答案的VO))
        @GetMapping("/viewRelatedQuestion")
        @ApiOperation("查看相关问题")
        public Result<List<Integer>> viewRelatedQuestion(Integer question_id) {
            log.info("查看相关问题...");
            List<Integer> list = discussService.viewRelatedQuestion(question_id);
            return Result.success(list);
        }

    }
}