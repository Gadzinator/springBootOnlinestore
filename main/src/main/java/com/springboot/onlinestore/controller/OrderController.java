package com.springboot.onlinestore.controller;

import com.springboot.onlinestore.domain.dto.OrderRequestDto;
import com.springboot.onlinestore.domain.dto.OrderResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "OrderController", description = "Working with orders")
@SecurityRequirement(name = "bearerAuth")
@ApiResponse(
		responseCode = "400",
		description = "Invalid input",
		content = @Content)
@ApiResponse(
		responseCode = "401",
		description = "User not authorized",
		content = @Content)
@ApiResponse(
		responseCode = "403",
		description = "Forbidden - Not enough permissions",
		content = @Content)
public interface OrderController {

	@Operation(summary = "Save order")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Order saved successfully",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = OrderResponseDto.class))
					})
	})
	@PreAuthorize("isAuthenticated()")
	@PostMapping
	ResponseEntity<?> save(@RequestBody(required = false) @Valid OrderRequestDto orderRequestDto);

	@Operation(summary = "Find order by id")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Order found by id",
					content = {
							@Content(
									mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = OrderResponseDto.class)))
					})
	})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/id/{id}")
	ResponseEntity<OrderResponseDto> findById(@PathVariable(value = "id") Long id);

	@Operation(summary = "Find all orders")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Found orders",
					content = {
							@Content(
									mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = OrderResponseDto.class)))
					})
	})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{page}/{size}")
	ResponseEntity<List<OrderResponseDto>> findAll(@PathVariable("page") int page, @PathVariable("size") int size);

	@Operation(summary = "Update order")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Order update",
					content = {
							@Content(
									mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = OrderResponseDto.class)))
					})
	})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping
	ResponseEntity<?> update(@RequestBody OrderRequestDto orderRequestDto);

	@Operation(summary = "Delete order by id")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Delete order",
					content = @Content()
			)
	})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteById(@PathVariable("id") Long id);
}
