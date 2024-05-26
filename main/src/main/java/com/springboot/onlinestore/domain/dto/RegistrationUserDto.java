package com.springboot.onlinestore.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationUserDto {

	private long id;

	@NotBlank(message = "name should not be blank")
	@Size(min = 2, max = 30, message = "name length should be between 2 and 30 characters")
	private String name;

	@NotBlank(message = "password should not be blank")
	@Size(min = 6, max = 20, message = "password length should be between 6 and 20 characters")
	private String password;

	@NotBlank(message = "confirmPassword should not be blank")
	@Size(min = 6, max = 20, message = "confirmPassword length should be between 6 and 20 characters")
	private String confirmPassword;

	@Email(message = "email should be valid")
	@NotBlank(message = "email should not be blank")
	private String email;
}
