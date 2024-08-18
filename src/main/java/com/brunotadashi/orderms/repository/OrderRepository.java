package com.brunotadashi.orderms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.brunotadashi.orderms.entity.Order;

public interface OrderRepository extends MongoRepository<Order, Long> {
  Page<Order> findAllByCustomerId(Long customerId, PageRequest pageRequest);

}
