package com.brunotadashi.orderms.controller.dto;

import java.math.BigDecimal;

import com.brunotadashi.orderms.entity.Order;

public record OrderResponse(Long orderId, Long customerId, BigDecimal total) {
  public static OrderResponse fromEntity(Order entity) {
    return new OrderResponse(entity.getOrderId(), entity.getCustomerId(), entity.getTotal());
  }
}
