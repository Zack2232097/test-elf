package com.sky.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "用户上传的问题")
public class QuestionDTO implements Serializable {

    private Long questionId;//问题的id

    private Long userId;

    private String question;//这个必须是传入的
}
