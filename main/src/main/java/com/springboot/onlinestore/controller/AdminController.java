package com.springboot.onlinestore.controller;

import com.springboot.onlinestore.domain.dto.UserDto;
import com.springboot.onlinestore.domain.dto.UserRoleChangeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "AdminController", description = "Working with admins")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses(value = {
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
				content = @Content)
})
@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface AdminController {

	@Operation(summary = "Find user by id")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "User found by id",
					content = {
							@Content(
									mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
					})
	})
	@GetMapping("/id/{id}")
	ResponseEntity<UserDto> findById(@PathVariable(value = "id") Long id);

	@Operation(summary = "Find all user", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Users found",
					content = {
							@Content(
									mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
					})
	})
	@GetMapping()
	ResponseEntity<List<UserDto>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
										  @RequestParam(value = "size", defaultValue = "10") int size);

	@Operation(summary = "Find user by name")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "User found by id",
					content = {
							@Content(
									mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
					})
	})
	@GetMapping("/name/{name}")
	ResponseEntity<UserDto> findByName(@PathVariable(value = "name") String name);

	@Operation(summary = "Delete user by id")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "User delete",
					content = @Content())
	})
	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteById(@PathVariable(value = "id") Long id);

	@Operation(summary = "Change user role")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "User role changed",
					content = {
							@Content(
									mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
					})
	})
	@PreAuthorize("isAuthenticated()")
	@PutMapping("/changeUserRole")
	ResponseEntity<?> changeUserRole(@RequestBody UserRoleChangeRequest userRoleChangeRequest);
}
