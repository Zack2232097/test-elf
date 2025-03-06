package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    //setmealIds
    //select setmeal id from setmeal_dish where dish_id in (1, 2, 3, 4)
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

}
