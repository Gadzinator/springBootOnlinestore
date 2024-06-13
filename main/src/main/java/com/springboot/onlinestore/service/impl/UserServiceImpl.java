package com.springboot.onlinestore.service.impl;

import com.springboot.onlinestore.domain.dto.ChangePasswordRequest;
import com.springboot.onlinestore.domain.dto.RegistrationUserDto;
import com.springboot.onlinestore.domain.dto.UserDto;
import com.springboot.onlinestore.domain.entity.Role;
import com.springboot.onlinestore.domain.entity.User;
import com.springboot.onlinestore.exception.PasswordMismatchException;
import com.springboot.onlinestore.exception.UserNotFoundException;
import com.springboot.onlinestore.exception.UsernameNotUniqueException;
import com.springboot.onlinestore.mapper.UserMapper;
import com.springboot.onlinestore.repository.UserRepository;
import com.springboot.onlinestore.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;

	@Transactional
	@Override
	public UserDto createNewUser(RegistrationUserDto registrationUserDto) {
		log.info("Starting creating new user: " + registrationUserDto);
		validateUsernameIsUnique(registrationUserDto.getName());
		validatePasswordMatch(registrationUserDto.getPassword(), registrationUserDto.getConfirmPassword());
		validateEmailIsUnique(registrationUserDto.getEmail());

		User user = createUserFromRegistrationUserDto(registrationUserDto);
		userRepository.save(user);
		log.info("Finished creating user: " + user);

		return userMapper.mapToUserDto(user);
	}

	@Override
	public UserDto findById(long id) {
		log.info("Starting finding user by id: " + id);
		final UserDto userDto = userRepository.findById(id)
				.map(user -> userMapper.mapToUserDto(user))
				.orElseThrow(() -> new UserNotFoundException("User was not found by id " + id));
		log.info("Finished finding user by id: " + userDto);

		return userDto;
	}

	@Override
	public UserDto findByName(String name) {
		log.info("Starting finding user by name: " + name);
		final UserDto userDto = userRepository.findByName(name)
				.map(user -> userMapper.mapToUserDto(user))
				.orElseThrow(() -> new UserNotFoundException("User was not found by name " + name));
		log.info("Finished finding user by name: " + userDto);

		return userDto;
	}

	@Transactional
	@Override
	public Page<UserDto> findAll(Pageable pageable) {
		log.info("Start finding all users:");
		Page<User> usersPage = userRepository.findAll(pageable);

		if (usersPage.isEmpty()) {
			final String errorMessage = "Users were not found";
			log.error(errorMessage);
			throw new UserNotFoundException(errorMessage);
		}
		log.info("Finished finding all users: " + usersPage);

		return usersPage.map(userMapper::mapToUserDto);
	}

	@Override
	@Transactional
	public void changeUserRole(String userName, String newRole) {
		log.info("Starting work on changing a user's role: " + userName + " to the role of " + newRole);
		final User user = userRepository.findByName(userName)
				.orElseThrow(() -> new UsernameNotFoundException("There is no user with that name " + userName));
		user.setRole(Role.valueOf(newRole));
		userRepository.save(user);
		log.info("Changed user role: " + user + " to role " + user.getRole());
	}

	@Transactional
	public void deleteById(long id) {
		UserDto userDto = findById(id);

		if (userDto != null) {
			userRepository.deleteById(id);
		}
	}

	@Override
	@Transactional
	public void changePassword(String userName, ChangePasswordRequest changePasswordRequest) {
		log.info("Starting change password for user: " + userName);
		final User user = userRepository.findByName(userName)
				.orElseThrow(() -> new UserNotFoundException("User was not found by name " + userName));

		String enteredPassword = changePasswordRequest.getOldPassword();
		String storedPassword = user.getPassword();

		if (!passwordEncoder.matches(enteredPassword, storedPassword)) {
			final String errorMessage = "Password change failed: Passwords do not match for user: " + userName;
			log.error(errorMessage);
			throw new PasswordMismatchException(errorMessage);
		}
		user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
		log.info("Finished password changed successfully for user: " + userName);
	}

	private void validatePasswordMatch(String password, String confirmPassword) {
		log.info("Starting validating password match: " + password + ", and " + confirmPassword);

		if (!password.equals(confirmPassword)) {
			final String errorMessage = "Passwords do not match";
			log.error(errorMessage);
			throw new PasswordMismatchException(errorMessage);
		}
		log.info("Finished validating password math");
	}

	private void validateUsernameIsUnique(String name) {
		log.info("Starting validating email uniqueness");
		final Optional<User> optionalUser = userRepository.findByName(name);

		if (optionalUser.isPresent()) {
			final String errorMessage = "User with name " + name + " already exists";
			log.error(errorMessage);
			throw new UsernameNotUniqueException(errorMessage);
		}
		log.info("Finished validating email uniqueness");
	}

	private void validateEmailIsUnique(String email) {
		final Optional<User> optionalUser = userRepository.findUserByEmail(email);

		if (optionalUser.isPresent()) {
			final String errorMessage = "User with name " + email + " already exists";
			log.error(errorMessage);
			throw new UsernameNotUniqueException(errorMessage);
		}
	}

	private User createUserFromRegistrationUserDto(RegistrationUserDto registrationUserDto) {
		log.info("Starting creating user from registration user: " + registrationUserDto);
		User user = new User();
		user.setName(registrationUserDto.getName());
		user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
		user.setEmail(registrationUserDto.getEmail());
		user.setRole(Role.ROLE_USER);
		log.info("Finished creating user from registration user: " + user);

		return user;
	}
}
