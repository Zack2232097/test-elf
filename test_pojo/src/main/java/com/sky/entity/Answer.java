package com.sky.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "用户上传的问题")
public class Answer implements Serializable {
    private Long answerId;

    private Long questionId;

    private Long respondentId;//用户的id

    private String answer;//这个必须是传入的

    private LocalDateTime createTime;

    private Boolean isAccepted;

}
//redisTemplate
