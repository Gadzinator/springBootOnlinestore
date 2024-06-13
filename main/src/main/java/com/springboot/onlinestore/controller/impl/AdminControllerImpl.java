package com.springboot.onlinestore.controller.impl;

import com.springboot.onlinestore.controller.AdminController;
import com.springboot.onlinestore.domain.dto.UserDto;
import com.springboot.onlinestore.domain.dto.UserRoleChangeRequest;
import com.springboot.onlinestore.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "AdminController", description = "Administrator only")
@RestController
@AllArgsConstructor
@RequestMapping("/admins")
public class AdminControllerImpl implements AdminController {

	private final UserService userService;

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> findById(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
	}

	@Override
	@GetMapping()
	public ResponseEntity<List<UserDto>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
												 @RequestParam(value = "size", defaultValue = "10") int size) {
		final Pageable pageable = PageRequest.of(page, size);
		Page<UserDto> usersPage = userService.findAll(pageable);

		return new ResponseEntity<>(usersPage.getContent(), HttpStatus.OK);
	}

	@Override
	@GetMapping("/name")
	public ResponseEntity<UserDto> findByName(@RequestParam(value = "name") String name) {
		return new ResponseEntity<>(userService.findByName(name), HttpStatus.OK);
	}

	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable(value = "id") Long id) {
		userService.deleteById(id);

		return new ResponseEntity<>("The user has been deleted", HttpStatus.ACCEPTED);
	}

	@Override
	@PutMapping("/changeUserRole")
	public ResponseEntity<?> changeUserRole(@RequestBody UserRoleChangeRequest userRoleChangeRequest) {
		userService.changeUserRole(userRoleChangeRequest.getUserName(), userRoleChangeRequest.getNewRole());

		return new ResponseEntity<>("Role change", HttpStatus.OK);
	}
}
