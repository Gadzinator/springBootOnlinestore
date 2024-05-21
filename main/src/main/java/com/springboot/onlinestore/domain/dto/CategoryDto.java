package com.springboot.onlinestore.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDto {

	@JsonProperty("id")
	private long id;

	@JsonProperty("name")
	@NotBlank(message = "name should not be blank")
	@Size(min = 2, max = 50)
	private String name;
}
