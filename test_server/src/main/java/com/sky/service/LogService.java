package com.sky.service;

import org.springframework.web.multipart.MultipartFile;

public interface LogService {

    void logAnalysis(Long id);

    Long save(String url);
}
