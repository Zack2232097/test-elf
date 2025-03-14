package com.sky.service;

import com.sky.vo.HotQuestionView;

import java.util.List;

public interface HotQuestionService {

    List<HotQuestionView> getHotQuestions();

    void update(Integer status);
}
