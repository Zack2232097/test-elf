package com.sky.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "用户上传的问题")
public class AnswerDTO implements Serializable {
    private Long answerId;

    private Long questionId;

    private Long respondentId;//用户的id

    private String answer;//这个必须是传入的
}
