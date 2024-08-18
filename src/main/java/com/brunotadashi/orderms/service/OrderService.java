package com.brunotadashi.orderms.service;

import java.math.BigDecimal;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.brunotadashi.orderms.controller.dto.OrderResponse;
import com.brunotadashi.orderms.entity.Order;
import com.brunotadashi.orderms.entity.OrderItem;
import com.brunotadashi.orderms.listener.dto.OrderCreatedEvent;
import com.brunotadashi.orderms.repository.OrderRepository;

@Service
public class OrderService {
  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  public void save(OrderCreatedEvent event) {
    var order = new Order();
    order.setOrderId(event.orderId());
    order.setCustomerId(event.customerId());
    order.setTotal(getTotal(event));
    order.setItems(getOrderItems(event));

    this.orderRepository.save(order);
  }

  public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
    var orders = this.orderRepository.findAllByCustomerId(customerId, pageRequest);

    return orders.map(OrderResponse::fromEntity);
  }

  public BigDecimal findTotalOnOrderByCustomerId(Long customerId) {
    var aggregations = Aggregation.newAggregation(Aggregation.match(Criteria.where("customerId").is(customerId)),
        Aggregation.group().sum("total").as("total"));

    var response = mongoTemplate.aggregate(aggregations, "orders", Document.class);

    return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
  }

  private BigDecimal getTotal(OrderCreatedEvent event) {
    return event.items().stream().map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
        .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
  }

  private static List<OrderItem> getOrderItems(OrderCreatedEvent event) {
    return event.items().stream().map(item -> new OrderItem(item.product(), item.quantity(), item.price())).toList();
  }

}
