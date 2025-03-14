package com.sky.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "用户上传的问题")
public class HotQuestionView implements Serializable {
    private String questionText;
    private List<String> answers;
}
