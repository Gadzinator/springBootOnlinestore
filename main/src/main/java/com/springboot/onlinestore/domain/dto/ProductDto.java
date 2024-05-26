package com.springboot.onlinestore.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.onlinestore.utils.DateConstant;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductDto {

	private long id;

	@NotBlank(message = "name should not be blank")
	@Size(max = 30, message = "name length should not exceed 255 characters")
	private String name;

	@NotBlank(message = "brand should not be blank")
	@Size(max = 30, message = "brand length should not exceed 255 characters")
	private String brand;

	@NotBlank(message = "description should not be blank")
	@Size(max = 255, message = "description length should not exceed 255 characters")
	private String description;

	@NotBlank(message = "category should not be blank")
	private String category;

	@Min(value = 0)
	private int price;

	@JsonFormat(pattern = DateConstant.DEFAULT_DATE_PATTERN)
	@NotBlank(message = "created should not be blank")
	private String created;

	@NotBlank(message = "availability should not be blank")
	@JsonProperty("isAvailable")
	private boolean isAvailable;

	@NotBlank(message = "received should not be blank")
	@JsonFormat(pattern = DateConstant.DEFAULT_DATE_PATTERN)
	private String received;
}
