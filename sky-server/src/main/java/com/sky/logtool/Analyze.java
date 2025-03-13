package com.sky.logtool;

import com.sky.entity.TestCase;
import com.sky.mapper.CaseMapper;
import com.sky.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class Analyze {
    @Autowired
    private UploadUtil uploadUtil;
    @Autowired
    private CaseMapper caseMapper;

    public void statisticStandalone(File logFile, Long id) {


        List<String> currentSectionLog = new ArrayList<>();
        List<TestCase> listCase= new ArrayList<>();
        try (BufferedReader file = new BufferedReader(new FileReader(logFile))) {
            System.out.println("open log_file: " + logFile.getName());
            int sectionCounter = 1;
            boolean flagUsefulBlock = false;
            boolean flagAuthPass = false;
            int ctrChallengeRsp = 0;
            int ctrChallengeReq = 0;
            long tMgtReCnt = 0, tChallengeReq = 0, tChallengeRsp = 0, tAuthPass = 0;
            String line;
            int head = 0;
            String failreason = "Fail reasons from script:";

            while ((line = file.readLine()) != null) {
                currentSectionLog.add(line);
                line = line.trim();
                // head
                if (Keywords.MgtReCnt(line)) {
                    flagUsefulBlock = true;
                    head++;
                    tMgtReCnt = Util.extractAndConvertTimeToMilliseconds(line);
                    if (head == 2){
                        head--;
                        sectionCounter++;
                        TestCase testCase = new TestCase();
                        String url = uploadUtil.uploadCurrentSectionLog(currentSectionLog);
                        testCase.setFragment(url);
                        testCase.setSectionCounter((long) sectionCounter);
                        testCase.setLogId(id);
                        if (failreason.equals("Fail reasons from script:")) {
                            testCase.setFailReason("Fail reasons from script:...");
                        } else {
                            testCase.setFailReason(failreason);
                        }

                        listCase.add(testCase);

                        //保存片段,上传到OSS
                        currentSectionLog.clear();
                        //保存片段的第一行
                        currentSectionLog.add(line);
                    }
                }

                // body
                if (flagUsefulBlock) {
                    //用户的先验信息
                    if (Keywords.UserPreInfo(line) != null) {
                        failreason = failreason + Keywords.UserPreInfo(line);
                    }

                    if (Keywords.ChallengeReq(line)) {
                        ctrChallengeReq++;
                        tChallengeReq = Util.extractAndConvertTimeToMilliseconds(line);
                    }

                    if (Keywords.ChallengeRsp(line)) {
                        ctrChallengeRsp++;
                        tChallengeRsp = Util.extractAndConvertTimeToMilliseconds(line);
                    }

                    // tail
                    if (Keywords.AuthPass(line)) {
                        flagAuthPass = true;
                        tAuthPass = Util.extractAndConvertTimeToMilliseconds(line);
                    }
                }
                if (flagAuthPass) {
                    TestCase testCase = new TestCase();
                    if (ctrChallengeReq >= 1 && ctrChallengeRsp >= 1) {
                        long mgtReCntChallengeReq = tChallengeReq - tMgtReCnt;
                        long challengeReqChallengeRsp = tChallengeRsp - tChallengeReq;
                        long challengeRspAuthPass = tAuthPass - tChallengeRsp;
                        long mgtReCntAuthPass = tAuthPass - tMgtReCnt;

                        testCase.setSectionCounter((long) sectionCounter);
                        testCase.setCtrChallengeReq(ctrChallengeReq);
                        testCase.setCtrChallengeRsp(ctrChallengeRsp);
                        testCase.setMgtReCntChallengeReq((int) mgtReCntChallengeReq);
                        testCase.setChallengeReqChallengeRsp((int) challengeReqChallengeRsp);
                        testCase.setChallengeRspAuthPass((int) challengeRspAuthPass);
                        testCase.setMgtReCntAuthPass((int) mgtReCntAuthPass);
                    }
                    //上传片段
                    String url = uploadUtil.uploadCurrentSectionLog(currentSectionLog);
                    testCase.setFragment(url);
                    testCase.setLogId(id);
                    //插入数据库,建议批量插入
                    listCase.add(testCase);

                    currentSectionLog.clear();
                    flagUsefulBlock = false;
                    flagAuthPass = false;
                    ctrChallengeRsp = 0;
                    ctrChallengeReq = 0;
                    sectionCounter++;
                    head = 0;
                    failreason = "Fail reasons from script:";

                }
            }
            //插入结果
            caseMapper.insertBatch(listCase);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
