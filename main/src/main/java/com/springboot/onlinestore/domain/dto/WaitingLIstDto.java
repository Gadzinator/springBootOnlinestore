package com.springboot.onlinestore.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WaitingLIstDto {

	private long id;

	@NotNull(message = "username should not be null")
	private String username;

	@NotNull(message = "productDto should not be null")
	@JsonProperty("products")
	private ProductDto productDto;
}
