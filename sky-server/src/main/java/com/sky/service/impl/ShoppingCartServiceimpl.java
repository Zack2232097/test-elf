package com.sky.service.impl;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.ShoppingCarMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
public class ShoppingCartServiceimpl implements ShoppingCartService {

    @Autowired
    private ShoppingCarMapper shoppingCarMapper;
    @Autowired
    private DishMapper dishMapper;

    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 判断当前加入到购物车中的商品是否已经存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        //通过threadlocal
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        //只会查到一条数据,使用list为了安全
        List<ShoppingCart> list = shoppingCarMapper.list(shoppingCart);

        // 不管需不需要考虑口味,根据联合四个因素(userid+shoppingCartDTO中的三个),
        // 只会查到一个. 传入的口味字符串不是java中的list,而是前端处理后传入的字符串
        // 上次加入了鸡腿(口味:"奥尔良加辣椒"),下一秒加入鸡腿(口味:"藤椒加辣椒")
        // 会作为两个数据

        // 如果已经存在，只需要将数量加一
        if (list != null && !list.isEmpty()) {
            //获得查询到的对象
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            //将结果插入
            shoppingCarMapper.updateNumberById(cart);
        }else{
            //如果不存在
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                //为了图片等信息,从数据库拿到dish
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
                //插入
                shoppingCarMapper.insert(shoppingCart);

            }
            //不添加套餐
        }


    }

    @Override
    public List<ShoppingCart> showShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        return shoppingCarMapper.list(shoppingCart);


    }
}
