package com.springboot.onlinestore.service.impl;

import com.springboot.onlinestore.domain.dto.OrderRequestDto;
import com.springboot.onlinestore.domain.dto.OrderResponseDto;
import com.springboot.onlinestore.domain.dto.ProductDto;
import com.springboot.onlinestore.domain.dto.UserDto;
import com.springboot.onlinestore.domain.entity.Order;
import com.springboot.onlinestore.domain.entity.Product;
import com.springboot.onlinestore.domain.entity.User;
import com.springboot.onlinestore.exception.OrderNotFoundException;
import com.springboot.onlinestore.mapper.OrderMapper;
import com.springboot.onlinestore.mapper.ProductMapper;
import com.springboot.onlinestore.mapper.UserMapper;
import com.springboot.onlinestore.repository.OrderRepository;
import com.springboot.onlinestore.service.OrderService;
import com.springboot.onlinestore.service.ProductService;
import com.springboot.onlinestore.service.UserService;
import com.springboot.onlinestore.utils.DateConstant;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

	private final UserService userService;
	private final OrderRepository orderRepository;
	private final ProductService productService;
	private final ProductMapper productMapper;
	private final OrderMapper orderMapper;
	private final UserMapper userMapper;

	@Transactional
	@Override
	public void save(OrderRequestDto orderRequestDto) {
		log.info("Start adding an order: " + orderRequestDto);
		final UserDto userDto = userService.findById(orderRequestDto.getUserId());
		final User user = userMapper.mapToUser(userDto);
		final Order order = orderMapper.mapToOrder(orderRequestDto);

		order.setUser(user);
		orderRepository.save(order);
		log.info("Finishing adding an order " + order);
	}

	@Transactional
	@Override
	public OrderResponseDto findById(long id) {
		log.info("Start finding order by id: " + id);
		final OrderResponseDto orderResponseDto = orderRepository.findById(id).map(order -> orderMapper.mapToOrderDto(order)).orElseThrow(
				() -> new OrderNotFoundException("Order not was found by id " + id));

		final UserDto userDto = userService.findById(orderResponseDto.getUserId());
		final int totalPrice = getTotalPrice(orderResponseDto.getProducts());

		orderResponseDto.setTotalPrice(totalPrice);
		orderResponseDto.setUserId(userDto.getId());
		log.info("Finishing finding order by id: " + orderResponseDto);

		return orderResponseDto;
	}

	@Transactional
	@Override
	public Page<OrderResponseDto> findAll(Pageable pageable) {
		log.info("Start finding all orders:");

		Page<Order> ordersPage = orderRepository.findAll(pageable);
		if (ordersPage.isEmpty()) {
			final String errorMessage = "Orders were not found";
			log.error(errorMessage);
			throw new OrderNotFoundException(errorMessage);
		}
		log.info("Finished finding all orders: " + ordersPage);

		return ordersPage.map(orderMapper::mapToOrderDto);
	}

	@Transactional
	@Override
	public void update(OrderRequestDto orderRequestDto) {
		log.info("Starting updating order: " + orderRequestDto);
		final OrderResponseDto orderResponseDto = findById(orderRequestDto.getId());

		if (orderResponseDto != null) {
			final Order order = orderMapper.mapToOrder(orderResponseDto);
			updateAllFields(order, orderRequestDto);

			orderRepository.saveAndFlush(order);
			log.info("Finished updated successfully: " + order);
		} else {
			final String errorMessage = "Order not was found by id " + orderRequestDto.getId();
			log.error(errorMessage);
			throw new OrderNotFoundException(errorMessage);
		}
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		log.info("Starting delete order by id: " + id);
		final OrderResponseDto orderResponseDto = findById(id);

		if (orderResponseDto != null) {
			orderRepository.deleteById(orderResponseDto.getId());

			log.info("Finished deleting order by id");
		} else {
			final String errorMessage = "Order not was found by id " + id;
			log.error(errorMessage);
			throw new OrderNotFoundException(errorMessage);
		}
	}

	private int getTotalPrice(List<ProductDto> products) {
		return products.stream()
				.mapToInt(ProductDto::getPrice)
				.sum();
	}

	private void updateAllFields(Order order, OrderRequestDto orderRequestDto) {
		log.info("Starting updating all fields: " + order);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateConstant.DEFAULT_DATE_PATTERN);
		final List<Product> products = getListProducts(orderRequestDto);
		order.setCreated(LocalDate.parse(orderRequestDto.getCreated(), dateTimeFormatter));
		order.setProducts(products);
		order.setOrderStatus(orderRequestDto.getOrderStatus());
		log.info("Finished updating all fields: " + order);
	}

	private List<Product> getListProducts(OrderRequestDto orderRequestDto) {
		log.info("Starting getting list products: " + orderRequestDto);
		List<Product> products = new ArrayList<>();
		final List<Long> productIds = orderRequestDto.getProductIds();

		for (Long productId : productIds) {
			final ProductDto productDto = productService.findById(productId);
			final Product product = productMapper.mapToProduct(productDto);
			products.add(product);
		}
		log.info("Finished getting list products: " + products);

		return products;
	}
}
