package com.sky.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import com.sky.enumeration.Reason;


import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "积分操作表//web开发课有相关，记录增删人员的记录")
public class PointsLog implements Serializable {
    private Long logId;

    private Long userId;

    private Long questionId;

    private Long answerId;

    private Integer pointsChange;

    private Reason reason;//由于pojo这个模块并没有引入common，所以不能使用它的枚举类

    private LocalDateTime createTime;

}
