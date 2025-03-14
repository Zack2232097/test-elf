package com.sky.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "用户上传的问题")
public class Question implements Serializable {

    private Long questionId;//问题的id

    private Long userId;//用户的id

    private String question;//这个必须是传入的

    private LocalDateTime createTime;

    private Long acceptedAnswerId;
}
