package com.springboot.onlinestore.controller.impl;

import com.springboot.onlinestore.controller.AuthController;
import com.springboot.onlinestore.domain.dto.ChangePasswordRequest;
import com.springboot.onlinestore.domain.dto.JwtRequest;
import com.springboot.onlinestore.domain.dto.JwtResponse;
import com.springboot.onlinestore.domain.dto.RegistrationUserDto;
import com.springboot.onlinestore.service.AuthService;
import com.springboot.onlinestore.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "AuthController", description = "Authentication")
@RequiredArgsConstructor
@RestController
public class AuthControllerImpl implements AuthController {

	private final UserService userService;
	private final AuthService authService;

	@Override
	@PostMapping("/auth")
	public ResponseEntity<?> createAuthToken(@RequestBody @Valid JwtRequest authRequest) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok(new JwtResponse(authService.createAuthToken(authRequest)));
	}

	@Override
	@PostMapping("/registration")
	public ResponseEntity<?> createNewUser(@RequestBody(required = false) RegistrationUserDto registrationUserDto) {
		if (registrationUserDto == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(userService.createNewUser(registrationUserDto), HttpStatus.CREATED);
	}

	@Override
	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, Principal principal) {
		userService.changePassword(principal.getName(), changePasswordRequest);

		return ResponseEntity.ok("Password changed successfully");
	}
}
