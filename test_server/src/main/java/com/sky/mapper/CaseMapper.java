package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CasePageQueryDTO;
import com.sky.entity.TestCase;
import com.sky.entity.TestLog;
import com.sky.enumeration.OperationType;
import com.sky.vo.CaseVO;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CaseMapper {


    void insertBatch(List<TestCase> listCase);

    Page<CaseVO> pageQuery(CasePageQueryDTO casePageQueryDTO);
}
