package com.springboot.onlinestore.service.impl;

import com.springboot.onlinestore.domain.UserPrincipal;
import com.springboot.onlinestore.domain.dto.JwtRequest;
import com.springboot.onlinestore.exception.AuthenticationFailedException;
import com.springboot.onlinestore.exception.UserNotFoundException;
import com.springboot.onlinestore.repository.UserRepository;
import com.springboot.onlinestore.service.AuthService;
import com.springboot.onlinestore.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

	@Override
	public String createAuthToken(JwtRequest authRequest) {
		log.info("Starting creating an authentication token: " + authRequest);
		UserDetails userDetails = loadUserByUsername(authRequest.getUsername());

		if (!passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
			final String errorMessage = "Password not valid";
			log.error(errorMessage);
			throw new AuthenticationFailedException(errorMessage);
		}

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, authRequest.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final boolean authenticated = authentication.isAuthenticated();

		if (!authenticated) {
			throw new AuthenticationFailedException("Authentication failed");
		}
		log.info("Finished creating the token for the user: " + userDetails);

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
