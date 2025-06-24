package com.sky.utils;

// 建议dashscope SDK的版本 >= 2.12.0
import java.util.Arrays;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.sky.properties.QianwenProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class QianwenAPI {
    @Autowired
    private QianwenProperties qianwenProperties;
    
    public GenerationResult callWithMessage(String msg) throws ApiException, NoApiKeyException, InputRequiredException {
        String key = qianwenProperties.getKey();
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")//设定角色和上下文
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content("基于下面单个测试用例的日志，帮我分析失败的原因" +
                        "要求：我只需要总结的结果（汉字字数控制在200个以内），不要把分析过程输出给我：" + msg)
                .build();
        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-0b1e5f5e463043029e6aac6afa4da5c6")
                .apiKey(key)
                // 此处以qwen-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-plus")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        return gen.call(param);
    }
}
