package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Tester;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TesterMapper {
    /**
     * 插入测试人员
     */
    @AutoFill(value = OperationType.INSERT)//自动填充createtime
    @Insert("insert into users (username, password, create_time) " +
            "VALUES " +
            "(#{username}, #{password}, #{createTime})")
    void insert(Tester tester);

    @Select("select * from users where username = #{username}")
    Tester getByUsername(String username);

//    @Insert("insert into test_logs (id, user_id, file, status, create_time) ")
//    void insert(Long a);

}
