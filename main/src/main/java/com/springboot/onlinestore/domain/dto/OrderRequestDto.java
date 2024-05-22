package com.springboot.onlinestore.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.onlinestore.domain.ValueOfEnum;
import com.springboot.onlinestore.domain.entity.OrderStatus;
import com.springboot.onlinestore.utils.DateConstant;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {

	private long id;

	private long userId;

	@JsonFormat(pattern = DateConstant.DEFAULT_DATE_PATTERN)
	@NotBlank(message = "created should not be blank")
	private String created;

	@Valid
	@NotNull(message = "productIds should not be null")
	@Size(min = 1, message = "at least one productsId should be present")
	private List<Long> productIds;

	@JsonProperty("orderStatus")
	@ValueOfEnum(enumClass = OrderStatus.class)
	@Enumerated(value = EnumType.STRING)
	private OrderStatus orderStatus;
}
