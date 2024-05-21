package com.springboot.onlinestore.domain.dto;

import lombok.Data;

@Data
public class JwtRequest {

	private String username;

	private String password;
}
