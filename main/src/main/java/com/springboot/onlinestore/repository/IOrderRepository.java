package com.springboot.onlinestore.repository;

import com.springboot.onlinestore.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<Order, Long>, CustomOrderRepository {
}
