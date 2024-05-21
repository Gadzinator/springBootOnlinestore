package com.springboot.onlinestore.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.onlinestore.utils.DateConstant;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponseDto {

	private long id;

	private long userId;

	@JsonFormat(pattern = DateConstant.DEFAULT_DATE_PATTERN)
	@NotBlank(message = "created should not be blank")
	private String created;

	@JsonProperty("products")
	private List<ProductDto> products;

	@JsonProperty("order_status")
	private String orderStatus;

	private int totalPrice;
}
