package com.springboot.onlinestore.controller;

import com.springboot.onlinestore.domain.dto.OrderRequestDto;
import com.springboot.onlinestore.domain.dto.OrderResponseDto;
import com.springboot.onlinestore.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

	private final IOrderService orderService;

	@PostMapping
	public ResponseEntity<?> save(@RequestBody(required = false) @Valid OrderRequestDto orderRequestDto) {
		if (orderRequestDto == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		orderService.save(orderRequestDto);

		return new ResponseEntity<>("Order save", HttpStatus.CREATED);
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<OrderResponseDto> findById(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
	}

	@GetMapping("/{page}/{size}")
	public ResponseEntity<List<OrderResponseDto>> findAll(@PathVariable("page") int page, @PathVariable("size") int size) {
		final Pageable pageable = PageRequest.of(page, size);
		Page<OrderResponseDto> ordersPage = orderService.findAll(pageable);

		return new ResponseEntity<>(ordersPage.getContent(), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody OrderRequestDto orderRequestDto) {
		orderService.update(orderRequestDto);

		return new ResponseEntity<>(orderRequestDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
		orderService.deleteById(id);

		return new ResponseEntity<>("The order has been deleted", HttpStatus.ACCEPTED);
	}
}
