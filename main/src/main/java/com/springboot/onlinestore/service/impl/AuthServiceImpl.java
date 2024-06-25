package com.springboot.onlinestore.service.impl;

import com.springboot.onlinestore.domain.UserPrincipal;
import com.springboot.onlinestore.domain.dto.JwtRequest;
import com.springboot.onlinestore.event.AuthAction;
import com.springboot.onlinestore.event.AuthEvent;
import com.springboot.onlinestore.exception.AuthenticationFailedException;
import com.springboot.onlinestore.exception.UserNotFoundException;
import com.springboot.onlinestore.repository.UserRepository;
import com.springboot.onlinestore.service.AuthService;
import com.springboot.onlinestore.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

	private final JwtTokenUtils jwtTokenUtils;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	public String createAuthToken(JwtRequest authRequest) {
		log.info("Starting creating an authentication token: " + authRequest);
		UserDetails userDetails = loadUserByUsername(authRequest.getUsername());

		if (!passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
			final String errorMessage = "Password not valid";
			log.error(errorMessage);
			applicationEventPublisher.publishEvent(new AuthEvent(this, authRequest.getUsername(), AuthAction.LOGIN_FAILURE));
			throw new AuthenticationFailedException(errorMessage);
		}

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, authRequest.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final boolean authenticated = authentication.isAuthenticated();

		if (!authenticated) {
			final String errorMessage = "Authentication failed";
			log.error(errorMessage);
			applicationEventPublisher.publishEvent(new AuthEvent(this, authRequest.getUsername(), AuthAction.LOGIN_FAILURE));
			throw new AuthenticationFailedException(errorMessage);
		}
		log.info("Finished creating the token for the user: " + userDetails);
		applicationEventPublisher.publishEvent(new AuthEvent(this, authRequest.getUsername(), AuthAction.LOGIN_SUCCESS));

		return jwtTokenUtils.generateToken(userDetails);
	}

	@Override
	public UserDetails loadUserByUsername(String userName) {
		log.info("Starting loading a user by name: " + userName);
		final UserPrincipal userPrincipal = userRepository.findByName(userName)
				.map(UserPrincipal::new)
				.orElseThrow(() -> new UserNotFoundException(String.format("Username '%s' not found", userName)));
		log.info("Finalizing user upload by name: " + userPrincipal);

		return userPrincipal;
	}
}
