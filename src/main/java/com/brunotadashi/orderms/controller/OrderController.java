package com.brunotadashi.orderms.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brunotadashi.orderms.controller.dto.ApiResponse;
import com.brunotadashi.orderms.controller.dto.OrderResponse;
import com.brunotadashi.orderms.controller.dto.PaginationResponse;
import com.brunotadashi.orderms.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @GetMapping("/customers/{customerId}/orders")

  public ResponseEntity<ApiResponse<OrderResponse>> listOrders(
      @PathVariable Long customerId,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

    var pageResponse = this.orderService.findAllByCustomerId(customerId, PageRequest.of(page, pageSize));
    var totalOnOrders = this.orderService.findTotalOnOrderByCustomerId(customerId);

    return ResponseEntity.ok(new ApiResponse<>(
        Map.of("totalOnOrders", totalOnOrders), pageResponse.getContent(), PaginationResponse.fromPage(pageResponse)));
  }

}
