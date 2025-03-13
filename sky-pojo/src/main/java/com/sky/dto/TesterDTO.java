package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "测试人员信息")
public class TesterDTO implements Serializable {

    private Long id;

    @ApiModelProperty("用户名(唯一)")
    private String username;
}
