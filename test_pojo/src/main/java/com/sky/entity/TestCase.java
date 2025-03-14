package com.sky.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCase implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long logId;
    private Long sectionCounter;
    private String fragment;//图片都要从oss请求何况我的这个
    private Integer ctrChallengeReq;
    private Integer ctrChallengeRsp;
    private Integer mgtReCntChallengeReq;
    private Integer challengeReqChallengeRsp;
    private Integer challengeRspAuthPass;
    private Integer mgtReCntAuthPass;
    private String failReason;

}
