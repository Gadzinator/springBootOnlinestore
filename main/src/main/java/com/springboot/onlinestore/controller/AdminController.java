package com.springboot.onlinestore.controller;

import com.springboot.onlinestore.domain.dto.UserRoleChangeRequest;
import com.springboot.onlinestore.service.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admins")
public class AdminController {

	private IAdminService adminService;

	@Autowired
	public AdminController(IAdminService adminService) {
		this.adminService = adminService;
	}

	@PutMapping("/changeUserRole")
	public ResponseEntity<?> changeUserRole(@RequestBody UserRoleChangeRequest userRoleChangeRequest) {
		adminService.changeUserRole(userRoleChangeRequest.getUserName(), userRoleChangeRequest.getNewRole());
		return new ResponseEntity<>("Role change", HttpStatus.OK);
	}
}
