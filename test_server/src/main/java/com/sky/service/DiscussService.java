package com.sky.service;


import com.sky.dto.AnswerDTO;
import com.sky.dto.QuestionDTO;
import com.sky.entity.Answer;
import com.sky.entity.Question;

import java.util.List;

public interface DiscussService {


    void submitQuestion(QuestionDTO questionDTO);
    void submitAnswer(AnswerDTO answerDTO);

    List<Question> viewQuestion();

    List<Integer> viewRelatedQuestion(Integer questionId);

    List<Answer> viewAnswerbyQuestionId(List<Integer> questionIds);


    void addPoints(Long respondentId);
}
