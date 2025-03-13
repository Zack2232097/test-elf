package com.sky.task;

import com.alibaba.fastjson.JSON;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private WebSocketServer webSocketServer;

    //帮助下单用户付款,更改付款状态,微信支付未开发
    @Scheduled(cron = "0 * * * * ?")
    public void helpUserPay() {
        List<Orders> ordersList = orderMapper.getByStatus(Orders.PENDING_PAYMENT);
        if(ordersList != null && ordersList.size() > 0) {
            log.info("帮助下单用户付款,更改付款状态:{}", LocalDateTime.now());
            for (Orders orders : ordersList) {
                Orders ord1 = new Orders();
                BeanUtils.copyProperties(orders, ord1);
                Long id = orders.getId();//获得订单该id
                String number = orders.getNumber();//获得订单号
                ord1.setStatus(Orders.TO_BE_CONFIRMED);
                ord1.setPayStatus(Orders.PAID);
                ord1.setCheckoutTime(LocalDateTime.now());
                ord1.setAmount(new BigDecimal(0.01));
                //用update最好,但是写update写if麻烦
                orderMapper.delete(orders.getId());
                orderMapper.insert(ord1);

                //通过websocket向客户端(商家)推送消息
                Map map = new HashMap();
                map.put("type",1);//1表示米单提醒2表示客户催单
                map.put("orderid",id);
                map.put("content","订单号:"+number);
                String json= JSON.toJSONString(map);
                webSocketServer.sendToAllClient(json);
            }


        }
    }
}
