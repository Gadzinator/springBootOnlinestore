package com.springboot.onlinestore.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JwtRequest {

	@NotBlank(message = "username should not be blank")
	private String username;

	@NotBlank(message = "password should not be blank")
	private String password;
}
