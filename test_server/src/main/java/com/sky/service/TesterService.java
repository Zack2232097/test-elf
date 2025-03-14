package com.sky.service;


import com.sky.dto.TesterDTO;
import com.sky.dto.TesterLoginDTO;
import com.sky.entity.Tester;

public interface TesterService {
    void addTester(TesterDTO testerDTO);

    Tester login(TesterLoginDTO testerLoginDTO);
}
