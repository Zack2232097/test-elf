package com.sky.service.impl;

import com.sky.entity.QuestionAnswerCount;
import com.sky.mapper.questionMapper;
import com.sky.service.HotQuestionService;
import com.sky.vo.HotQuestionView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HotQuestionServiceImpl implements HotQuestionService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private questionMapper questionMapper;

    @Override
    public List<HotQuestionView> getHotQuestions() {
        Set<Object> questionIds = redisTemplate.opsForHash().keys("hot_questions");
        List<HotQuestionView> hotQuestionViews = new ArrayList<>();

        for (Object questionId : questionIds) {
            String questionText = (String) redisTemplate.opsForHash().get("hot_questions", questionId.toString());
            List<String> answers = redisTemplate.opsForList().range("hot_answers:" + questionId, 0, -1);

            HotQuestionView hotQuestionView = new HotQuestionView();
            hotQuestionView.setQuestionText(questionText);
            hotQuestionView.setAnswers(answers);

            hotQuestionViews.add(hotQuestionView);
        }

        return hotQuestionViews;
    }

    @Override
    public void update(Integer status) {
        log.info("设置热点问题参数: {}", status);
        List<QuestionAnswerCount> topQuestions = questionMapper.getTopFiveQuestions();

        redisTemplate.delete("hot_questions");

        for (QuestionAnswerCount question : topQuestions) {
            //放入hot_questions表,id为键,内容为value
            redisTemplate.opsForHash().put("hot_questions", question.getQuestionId().toString(), question.getQuestion());
            List<String> answers = questionMapper.getById(question.getQuestionId());
            String key = "hot_answers:" + question.getQuestionId();
            redisTemplate.delete(key);
            redisTemplate.opsForList().rightPushAll(key, answers);
        }
    }
}
