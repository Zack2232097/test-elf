package com.sky.listener;
import com.sky.service.DiscussService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class AnswerSubmitListener {
    @Autowired
    private DiscussService discussService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "answer.submit.queue", durable = "true"),
            exchange = @Exchange(name = "answer", type = ExchangeTypes.TOPIC),
            key = "submit.success"
    ))
    public void listenAnswerSubmit(Long respondentId) {
        // 为respondentId增加积分
        discussService.addPoints(respondentId);
    }
}
