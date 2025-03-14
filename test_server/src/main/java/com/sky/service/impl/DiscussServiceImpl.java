package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.AnswerDTO;
import com.sky.dto.QuestionDTO;
import com.sky.entity.Answer;
import com.sky.entity.Question;
import com.sky.mapper.DiscussMapper;
import com.sky.service.DiscussService;
import com.sky.utils.PythonAPI;
import org.apache.ibatis.annotations.Property;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
public class DiscussServiceImpl implements DiscussService {
    @Autowired
    private DiscussMapper discussMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void submitQuestion(QuestionDTO questionDTO) {
        Question question = new Question();
        BeanUtils.copyProperties(questionDTO, question);
        //获得提问者的id
        question.setUserId(BaseContext.getCurrentId());
        discussMapper.insert(question);
    }

    @Override
    public void submitAnswer(AnswerDTO answerDTO) {
        Answer answer = new Answer();
        BeanUtils.copyProperties(answerDTO, answer);
        discussMapper.insertAnswer(answer);
        rabbitTemplate.convertAndSend("answer", "submit.success", answer.getRespondentId());
    }
    //秒杀场景


    @Override
    public List<Question> viewQuestion() {
        return discussMapper.list();
    }

    @Override
    public List<Integer> viewRelatedQuestion(Integer questionId) {
        // 先从数据库获取sentence, 然后
        String sentence = discussMapper.getQuestionById(questionId);
        // 调用PythonAPI
        double[] similar_elements = PythonAPI.getSentenceEmbedding(sentence, questionId + 600);
        //形如[0.9, 2.1, ...]
        List<Integer> list = new ArrayList<>();
        if (similar_elements != null) {
            for (double similarElement : similar_elements) {
                list.add((int) similarElement);
            }
            System.out.println("similar_elements: " + Arrays.toString(similar_elements));
        }
        return list;
    }

    @Override
    public List<Answer> viewAnswerbyQuestionId(List<Integer> questionIds) {
        return discussMapper.getAnswersByQuestionIds(questionIds);
    }

    @Override
    public void addPoints(Long respondentId) {
        discussMapper.addPointsById(respondentId);
    }
}
