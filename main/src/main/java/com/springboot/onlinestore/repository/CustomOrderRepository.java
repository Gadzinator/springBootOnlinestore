package com.springboot.onlinestore.repository;

import com.springboot.onlinestore.domain.entity.Order;
import com.springboot.onlinestore.domain.entity.Product;

import java.util.List;

public interface CustomOrderRepository {

	List<Product> findProductsOrderId(long id);

	List<Order> findOrdersByProductId(long productId);
}
