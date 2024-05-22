package com.springboot.onlinestore.service.impl;

import com.springboot.onlinestore.domain.dto.PasswordChangeRequest;
import com.springboot.onlinestore.domain.dto.RegistrationUserDto;
import com.springboot.onlinestore.domain.dto.UserDto;
import com.springboot.onlinestore.domain.entity.Role;
import com.springboot.onlinestore.domain.entity.User;
import com.springboot.onlinestore.exception.PasswordMismatchException;
import com.springboot.onlinestore.exception.UserNotFoundException;
import com.springboot.onlinestore.exception.UsernameNotUniqueException;
import com.springboot.onlinestore.mapper.IUserMapper;
import com.springboot.onlinestore.repository.IOrderRepository;
import com.springboot.onlinestore.repository.IUserRepository;
import com.springboot.onlinestore.service.IUserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@AllArgsConstructor
@Service
public class UserService implements IUserService {

	private IUserRepository userRepository;

	private IOrderRepository orderRepository;

	private final PasswordEncoder passwordEncoder;

	private IUserMapper userMapper;

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
			throw new UserNotFoundException("Users were not found");
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
	public void changePassword(String userName, PasswordChangeRequest passwordChangeRequest) {
		log.info("Starting change password for user: " + userName);

		String enteredPassword = passwordChangeRequest.getOldPassword();
		final User user = userRepository.findByName(userName)
				.orElseThrow(() -> new UserNotFoundException("User was not found by name " + userName));

		String storedPassword = user.getPassword();

		if (!passwordEncoder.matches(enteredPassword, storedPassword)) {

			log.error("Password change failed: Passwords do not match for user: " + userName);

			throw new PasswordMismatchException("Passwords do not match");
		}
		user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
		userRepository.save(user);

		log.info("Finished password changed successfully for user: " + userName);
	}

	private void validatePasswordMatch(String password, String confirmPassword) {
		log.info("Starting validating password match: " + password + ", and " + confirmPassword);

		if (!password.equals(confirmPassword)) {

			log.error("Passwords do not match");

			throw new PasswordMismatchException("Passwords do not match");
		}
		log.info("Finished validating password math");
	}

	private void validateUsernameIsUnique(String name) {
		log.info("Starting validating email uniqueness");

		final Optional<User> optionalUser = userRepository.findByName(name);
		if (optionalUser.isPresent()) {

			log.error("User with name " + name + " already exists");

			throw new UsernameNotUniqueException("User with name " + name + " already exists");
		}
		log.info("Finished validating email uniqueness");
	}

	private void validateEmailIsUnique(String email) {
		final Optional<User> optionalUser = userRepository.findUserByEmail(email);
		if (optionalUser.isPresent()) {
			throw new UsernameNotUniqueException("User with name " + email + " already exists");
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
