package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseVO implements Serializable {

    private Long id;
    private Long logId;
    private Long sectionCounter;
    private String fragment;//浏览器获得的url就能拿到数据
    private Integer ctrChallengeReq;
    private Integer ctrChallengeRsp;
    private Integer mgtReCntChallengeReq;
    private Integer challengeReqChallengeRsp;
    private Integer challengeRspAuthPass;
    private Integer mgtReCntAuthPass;
    private String failReason;//failreason也是一样的
}
