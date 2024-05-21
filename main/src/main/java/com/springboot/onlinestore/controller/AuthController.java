package com.springboot.onlinestore.controller;

import com.springboot.onlinestore.domain.dto.JwtRequest;
import com.springboot.onlinestore.domain.dto.JwtResponse;
import com.springboot.onlinestore.domain.dto.PasswordChangeRequest;
import com.springboot.onlinestore.domain.dto.RegistrationUserDto;
import com.springboot.onlinestore.service.IAuthService;
import com.springboot.onlinestore.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class AuthController {

	private final IUserService userService;

	private final IAuthService authService;

	@PostMapping("/auth")
	public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok(new JwtResponse(authService.createAuthToken(authRequest)));
	}

	@PostMapping("/registration")
	public ResponseEntity<?> createNewUser(@RequestBody(required = false) RegistrationUserDto registrationUserDto) {
		if (registrationUserDto == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(userService.createNewUser(registrationUserDto), HttpStatus.CREATED);
	}

	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest, Principal principal) {
		userService.changePassword(principal.getName(), passwordChangeRequest);

		return ResponseEntity.ok("Password changed successfully");
	}
}
