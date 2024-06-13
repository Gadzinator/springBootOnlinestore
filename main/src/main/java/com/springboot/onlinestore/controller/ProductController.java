package com.springboot.onlinestore.controller;

import com.springboot.onlinestore.domain.dto.ProductDto;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Tag(name = "ProductController", description = "Working with products")
@ApiResponse(
		responseCode = "400",
		description = "Invalid input",
		content = @Content)
public interface ProductController {

	@Operation(summary = "Save product", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Product saved successfully",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = ProductDto.class))
					}),
			@ApiResponse(
					responseCode = "401",
					description = "User not authorized",
					content = @Content),
			@ApiResponse(
					responseCode = "403",
					description = "Forbidden - Not enough permissions",
					content = @Content),

	})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	ResponseEntity<?> save(@RequestBody(required = false) ProductDto productDto);

	@Operation(summary = "Find all products")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Product found",
					content = {
							@Content(
									mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
					})
	})
	@GetMapping("/all")
	ResponseEntity<List<ProductDto>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
											 @RequestParam(value = "size", defaultValue = "10") int size);

	@Operation(summary = "Find product by id")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Product found by id",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = ProductDto.class))
					})
	})
	@GetMapping("/{id}")
	ResponseEntity<?> findById(@PathVariable(value = "id") Long id);


	@Operation(summary = "Find product by name")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Product found by name",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = ProductDto.class))
					})
	})
	@GetMapping("/name")
	ResponseEntity<?> findByName(@RequestParam(value = "name") String name);

	@Operation(summary = "Find by params")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Products found by params",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = ProductDto.class))
					})
	})
	@GetMapping
	ResponseEntity<List<ProductDto>> findByParams(@RequestParam(required = false) Map<String, String> params);

	@Operation(summary = "Update product", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Product update",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = ProductDto.class))
					}),
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
	@PutMapping
	ResponseEntity<?> update(@RequestBody ProductDto updateProductDto);

	@Operation(summary = "Delete product", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Product deleted"
			),
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
	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteByID(@PathVariable("id") Long id);
}
