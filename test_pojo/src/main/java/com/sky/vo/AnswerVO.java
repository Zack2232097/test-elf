package com.sky.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "用户上传的问题")
public class AnswerVO implements Serializable {

    private Long respondentId;//用户的id

    private String answer;//这个必须是传入的

    private Boolean isAccepted;

}
