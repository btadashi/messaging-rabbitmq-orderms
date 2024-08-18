package com.brunotadashi.orderms.listener;

import static com.brunotadashi.orderms.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.brunotadashi.orderms.listener.dto.OrderCreatedEvent;
import com.brunotadashi.orderms.service.OrderService;

@Component
public class OrderCreatedListener {

  @Autowired
  private OrderService orderService;

  private final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);

  @RabbitListener(queues = ORDER_CREATED_QUEUE)
  public void listen(Message<OrderCreatedEvent> message) {
    logger.info("Message consumed: {}", message);

    this.orderService.save(message.getPayload());
  }

}
