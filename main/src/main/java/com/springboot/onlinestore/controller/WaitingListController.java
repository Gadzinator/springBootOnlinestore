package com.springboot.onlinestore.controller;

import com.springboot.onlinestore.domain.dto.WaitingLIstDto;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "WaitingListController", description = "Working with waiting lists")
public interface WaitingListController {

	@Operation(summary = "Save waiting list", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Waiting list saved successfully",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = WaitingLIstDto.class))
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
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/{productId}/{username}")
	ResponseEntity<?> save(@PathVariable(value = "productId") Long productId, @PathVariable(value = "username") String username);

	@Operation(summary = "Find waiting list by id", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Waiting list found by id",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = WaitingLIstDto.class))
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
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}")
	ResponseEntity<?> findById(@PathVariable(value = "id") Long id);

	@Operation(summary = "Find all waiting list", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Waiting lists found",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = WaitingLIstDto.class))
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
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{page}/{size}")
	ResponseEntity<?> findAll(@PathVariable("page") int page, @PathVariable("size") int size);

	@Operation(summary = "Update waiting list", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Waiting list updated",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = WaitingLIstDto.class))
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
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping
	ResponseEntity<?> update(@RequestBody WaitingLIstDto waitingLIstDto);

	@Operation(summary = "Delete waiting list", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Waiting list deleted",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = WaitingLIstDto.class))
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
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{id}")
	ResponseEntity<?> delete(@PathVariable("id") Long id);
}
