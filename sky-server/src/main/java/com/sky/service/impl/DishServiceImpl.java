package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;


    @Transactional//SkyApplication已经开启注解方式的事务管理，所以治理直接加注释就可以
    //由于需要兼顾口味表，所以需要事务管理
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        //只能从dishDTO拷贝copy共有的字段
        BeanUtils.copyProperties(dishDTO, dish);

        //向Dish表插入一条数据
        dishMapper.insert(dish);

        //新增菜品获得dish-id，传入给下面的口味表
        Long dishId = dish.getId();


        //向口味插入插入n条
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            //遍历插入dishId
            flavors.forEach(flavor ->
                    flavor.setDishId(dishId));

            dishFlavorMapper.insertBatch(flavors);

        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

}
