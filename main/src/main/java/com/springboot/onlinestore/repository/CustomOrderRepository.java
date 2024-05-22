package com.springboot.onlinestore.repository;

import com.springboot.onlinestore.domain.entity.Order;

import java.util.List;

public interface CustomOrderRepository {

	List<Order> findOrdersByProductId(long productId);
}
