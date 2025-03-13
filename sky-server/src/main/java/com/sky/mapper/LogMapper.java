package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.TestLog;
import com.sky.entity.Tester;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LogMapper {

    @AutoFill(value = OperationType.INSERT)//自动填充createtime@AutoFill(value = OperationType.INSERT)//自动填充createtime
    @Insert("insert into test_logs (user_id, file, create_time) " +
            "VALUES " +
            "(#{userId}, #{file}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(TestLog testLog);


    @Select("select file from test_logs where id = #{id}")
    String selectById(Long id);
}
