package com.sky.service;

import com.sky.dto.CasePageQueryDTO;
import com.sky.result.PageResult;

public interface CaseService {


    PageResult pageQuery(CasePageQueryDTO casePageQueryDTO);
}
