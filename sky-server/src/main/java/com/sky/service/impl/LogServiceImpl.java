package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.context.BaseContext;
import com.sky.dto.TesterDTO;
import com.sky.dto.TesterLoginDTO;
import com.sky.entity.TestLog;
import com.sky.entity.Tester;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.logtool.Analyze;
import com.sky.mapper.LogMapper;
import com.sky.mapper.TesterMapper;
import com.sky.service.LogService;
import com.sky.service.TesterService;
import com.sky.utils.AliOssUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private AliOssUtil aliOssUtil;
    @Autowired
    private Analyze analyze;
    @Override
    public void logAnalysis(Long id) {
        //根据logID查询url
        String url = logMapper.selectById(id);
        String filename = url.substring(url.lastIndexOf("/") + 1);
        String savePath = "D:/A_Sky/post-side/sky-take-out/sky-server/src/main/java/com/sky/logtool/" + filename;
        //下载
        try {
            aliOssUtil.download(url, savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //日志分析
        File log = new File(savePath);
        analyze.statisticStandalone(log, id);



    }

    @Override
    public Long save(String url) {
        TestLog testLog = new TestLog();
        testLog.setFile(url);
        Long userId = BaseContext.getCurrentId();
        testLog.setUserId(userId);

        logMapper.insert(testLog);
        return testLog.getId();

    }
}
