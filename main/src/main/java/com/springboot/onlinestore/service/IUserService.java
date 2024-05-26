package com.springboot.onlinestore.service;

import com.springboot.onlinestore.domain.dto.ChangePasswordRequest;
import com.springboot.onlinestore.domain.dto.RegistrationUserDto;
import com.springboot.onlinestore.domain.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

	UserDto createNewUser(RegistrationUserDto registrationUserDto);

	UserDto findById(long id);

	Page<UserDto> findAll(Pageable pageable);

	void changeUserRole(String userName, String newRole);

	UserDto findByName(String name);

	void deleteById(long id);

	void changePassword(String userName, ChangePasswordRequest changePasswordRequest);
}
