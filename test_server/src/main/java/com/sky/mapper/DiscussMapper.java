package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Answer;
import com.sky.entity.Question;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;


@Mapper
public interface DiscussMapper {

    @AutoFill(OperationType.INSERT)
    @Insert("insert into questions (question_id, user_id, question, create_time, accepted_answer_id) " +
            "values " +
            "(#{questionId}, #{userId}, #{question}, #{createTime}, #{acceptedAnswerId})")
    void insert(Question question);

    @Select("select * from questions order by create_time desc")
    List<Question> list();

    @Select("select question from questions where question_id = #{questionId}")
    String getQuestionById(Integer questionId);

    List<Answer> getAnswersByQuestionIds(List<Integer> questionIds);

    @AutoFill(OperationType.INSERT)
    @Insert("insert into answers (answer_id, question_id, respondent_id, answer, create_time) " +
            "values " +
            "(#{answerId}, #{questionId}, #{respondentId}, #{answer}, #{createTime})")
    void insertAnswer(Answer answer);

    @Update("update users set bounty = bounty + 1 where id = #{respondentId}")
    void addPointsById(Long respondentId);
}
