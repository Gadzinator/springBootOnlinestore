package com.springboot.onlinestore.controller;

import com.springboot.onlinestore.domain.dto.ChangePasswordRequest;
import com.springboot.onlinestore.domain.dto.JwtRequest;
import com.springboot.onlinestore.domain.dto.ProductDto;
import com.springboot.onlinestore.domain.dto.RegistrationUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Tag(name = "AuthController", description = "Working with auth")
public interface AuthController {

	@Operation(summary = "Create auth token")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Token created",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = ProductDto.class))
					}),
			@ApiResponse(
					responseCode = "400",
					description = "Invalid input",
					content = @Content),
			@ApiResponse(
					responseCode = "401",
					description = "User not authorized",
					content = @Content),
			@ApiResponse(
					responseCode = "403",
					description = "Forbidden - Not enough permissions",
					content = @Content),

	})
	@PostMapping("/auth")
	ResponseEntity<?> createAuthToken(@RequestBody @Valid JwtRequest authRequest);

	@Operation(summary = "Registration user")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "User registration was successful",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = ProductDto.class))
					}),
			@ApiResponse(
					responseCode = "400",
					description = "Invalid input",
					content = @Content),
			@ApiResponse(
					responseCode = "401",
					description = "User not authorized",
					content = @Content),
			@ApiResponse(
					responseCode = "403",
					description = "Forbidden - Not enough permissions",
					content = @Content),

	})
	@PostMapping("/registration")
	ResponseEntity<?> createNewUser(@RequestBody(required = false) RegistrationUserDto registrationUserDto);

	@Operation(summary = "Registration user")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "User registration was successful",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = ProductDto.class))
					}),
			@ApiResponse(
					responseCode = "400",
					description = "Invalid input",
					content = @Content),
			@ApiResponse(
					responseCode = "401",
					description = "User not authorized",
					content = @Content),
			@ApiResponse(
					responseCode = "403",
					description = "Forbidden - Not enough permissions",
					content = @Content),

	})

	@PostMapping("/changePassword")
	ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, Principal principal);
}
