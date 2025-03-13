package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    //自动填充时间和创建人，因为只传入了部分信息
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    //DishPageQueryDTO中的page和size交给了插件，只要考虑其它三个选项就行
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    List<Dish> list(Dish dish);

    @Select("select * from dish where id = #{dishId}")
    Dish getById(Long dishId);
}
