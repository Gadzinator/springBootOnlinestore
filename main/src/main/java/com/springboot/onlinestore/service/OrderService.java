package com.springboot.onlinestore.service;

import com.springboot.onlinestore.domain.dto.OrderRequestDto;
import com.springboot.onlinestore.domain.dto.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

	void save(OrderRequestDto orderRequestDto);

	OrderResponseDto findById(long id);

	Page<OrderResponseDto> findAll(Pageable pageable);

	void update(OrderRequestDto orderRequestDto);

	void deleteById(long id);
}
