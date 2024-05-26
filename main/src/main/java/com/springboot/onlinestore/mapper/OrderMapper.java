package com.springboot.onlinestore.mapper;

import com.springboot.onlinestore.domain.dto.OrderRequestDto;
import com.springboot.onlinestore.domain.dto.OrderResponseDto;
import com.springboot.onlinestore.domain.dto.ProductDto;
import com.springboot.onlinestore.domain.entity.Order;
import com.springboot.onlinestore.domain.entity.OrderStatus;
import com.springboot.onlinestore.domain.entity.Product;
import com.springboot.onlinestore.domain.entity.User;
import com.springboot.onlinestore.utils.DateConstant;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, CategoryMapper.class})
public interface OrderMapper {

	@Mapping(target = "products", source = "products")
	@Mapping(target = "created", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(target = "user", source = "userId", qualifiedByName = "userIdToUser")
	Order mapToOrder(OrderResponseDto orderResponseDto);

	@Mapping(source = "products", target = "products")
	@Mapping(source = "created", target = "created", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
	@Mapping(target = "totalPrice", expression = "java(getTotalPrice(orderResponseDto.getProducts()))")
	OrderResponseDto mapToOrderDto(Order order);

	@Mapping(source = "productIds", target = "products")
	@Mapping(target = "created", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(source = "orderStatus", target = "orderStatus", qualifiedByName = "mapOrderStatusToString")
	Order mapToOrder(OrderRequestDto orderRequestDto);

	@InheritInverseConfiguration
	@Mapping(source = "products", target = "productIds")
	@Mapping(source = "created", target = "created", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(source = "orderStatus", target = "orderStatus", qualifiedByName = "mapStringToOrderStatus")
	OrderRequestDto mapToOrderRequestDto(Order order);

	default List<Product> mapProductIdsToProducts(List<Long> productIds) {
		if (productIds == null) {
			return null;
		}
		return productIds.stream()
				.map(productId -> {
					Product product = new Product();
					product.setId(productId);
					return product;
				})
				.collect(Collectors.toList());
	}

	default List<Long> mapProductsToProductIds(List<Product> products) {
		if (products == null) {
			return null;
		}
		return products.stream()
				.map(Product::getId)
				.collect(Collectors.toList());
	}

	@Named("mapOrderStatusToString")
	default String mapOrderStatusToString(OrderStatus orderStatus) {
		return orderStatus != null ? orderStatus.name() : null;
	}

	@Named("mapStringToOrderStatus")
	default OrderStatus mapStringToOrderStatus(String orderStatus) {
		return orderStatus != null ? OrderStatus.valueOf(orderStatus) : null;
	}

	@Named("userIdToUser")
	default User userIdToUser(long userId) {
		User user = new User();
		user.setId(userId);
		return user;
	}

	@Named("userToUserId")
	default long userToUserId(User user) {
		return user.getId();
	}

	default int getTotalPrice(List<ProductDto> dtoList) {
		return dtoList.stream()
				.mapToInt(ProductDto::getPrice)
				.sum();
	}
}
