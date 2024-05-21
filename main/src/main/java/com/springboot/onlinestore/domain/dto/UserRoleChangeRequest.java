package com.springboot.onlinestore.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRoleChangeRequest {

	private String userName;

	@NotBlank(message = "newRole should not be blank")
	@Size(min = 4, max = 50)
	private String newRole;
}
