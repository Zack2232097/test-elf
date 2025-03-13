package com.sky.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.sky.properties.AliOssProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

//将java中的数据（如截取的片段（List集合）等转换为字符串，然后流）
@Component
public class UploadUtil {
    //不建议字段注入，不能手动创建UploadUtil，因为AliOssProperties
//    @Autowired
//    private AliOssProperties aliOSSProperties;

    //推荐使用构造函数注入
    private final AliOssProperties aliOSSProperties;

    public UploadUtil(AliOssProperties aliOSSProperties) {
        this.aliOSSProperties = aliOSSProperties;
    }


    public String uploadCurrentSectionLog(List<String> currentSectionLog) throws IOException {
        //获取阿里云OSS参数
        String endpoint = aliOSSProperties.getEndpoint();
        String accessKeyId = aliOSSProperties.getAccessKeyId();
        String accessKeySecret = aliOSSProperties.getAccessKeySecret();
        String bucketName = aliOSSProperties.getBucketName();

        // 将 currentSectionLog 转换为字符串
        StringBuilder logContent = new StringBuilder();
        for (String line : currentSectionLog) {
            logContent.append(line).append("\n");
        }

        // 将字符串转换为字节数组输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(logContent.toString().getBytes(StandardCharsets.UTF_8));


        // 避免文件覆盖,uuid统一唯一标识符加上.拓展名
        String fileName = "section"+UUID.randomUUID().toString() + ".txt";

        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);

        //文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }
}