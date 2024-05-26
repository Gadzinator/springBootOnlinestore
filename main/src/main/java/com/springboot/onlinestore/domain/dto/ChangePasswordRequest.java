package com.springboot.onlinestore.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

	@NotBlank(message = "oldPassword should not be blank")
	private String oldPassword;

	@NotBlank(message = "newPassword should not be blank")
	@Size(min = 6, max = 20, message = "newPassword length should be between 6 and 20 characters")
	private String newPassword;
}
