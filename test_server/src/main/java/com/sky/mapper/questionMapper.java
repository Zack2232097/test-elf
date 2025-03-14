package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Answer;
import com.sky.entity.Question;
import com.sky.entity.QuestionAnswerCount;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface questionMapper {


    List<QuestionAnswerCount> getTopFiveQuestions();

    @Select("select answer from answers where question_id = #{id}")
    List<String> getById(Integer id);
}
