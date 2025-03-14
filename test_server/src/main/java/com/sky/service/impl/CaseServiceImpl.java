package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.CasePageQueryDTO;
import com.sky.mapper.CaseMapper;
import com.sky.result.PageResult;
import com.sky.service.CaseService;
import com.sky.vo.CaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class CaseServiceImpl implements CaseService {
    @Autowired
    private CaseMapper caseMapper;

    @Override
    public PageResult pageQuery(CasePageQueryDTO casePageQueryDTO) {
        PageHelper.startPage(casePageQueryDTO.getPage(), casePageQueryDTO.getPageSize());
        Page<CaseVO> page = caseMapper.pageQuery(casePageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }
}
