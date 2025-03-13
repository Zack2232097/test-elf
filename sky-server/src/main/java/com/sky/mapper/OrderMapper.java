package com.sky.mapper;


import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {
    void insert(Orders orders);

    @Select("select * from orders where status = #{pendingPayment}")
    List<Orders> getByStatus(Integer pendingPayment);

    @Delete("delete from orders where id = #{id}")
    void delete(Long id);
}
