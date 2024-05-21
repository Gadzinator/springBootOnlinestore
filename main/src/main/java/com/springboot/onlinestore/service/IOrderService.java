package com.springboot.onlinestore.service;

import com.springboot.onlinestore.domain.dto.OrderRequestDto;
import com.springboot.onlinestore.domain.dto.OrderResponseDto;
import com.springboot.onlinestore.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {

	void save(OrderRequestDto orderRequestDto);

	OrderResponseDto findById(long id);

	Page<OrderResponseDto> findAll(Pageable pageable);

	void update(OrderRequestDto orderRequestDto);

	void deleteById(long id);
}
