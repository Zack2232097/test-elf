package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CasePageQueryDTO implements Serializable {

    private int page;

    private int pageSize;

    private Long logId;//必传递的， 不然你查询到别人的了

    //约定result：1为成功，数据中我们缺省表头reslut为，通过failreason就可以判定
    private Integer result;

}
