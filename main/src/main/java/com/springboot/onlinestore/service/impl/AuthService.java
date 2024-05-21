package com.springboot.onlinestore.service.impl;

import com.springboot.onlinestore.domain.MyUserPrincipal;
import com.springboot.onlinestore.domain.dto.JwtRequest;
import com.springboot.onlinestore.exception.AuthenticationFailedException;
import com.springboot.onlinestore.exception.UserNotFoundException;
import com.springboot.onlinestore.repository.IUserRepository;
import com.springboot.onlinestore.service.IAuthService;
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
public class AuthService implements IAuthService {

	private final JwtTokenUtils jwtTokenUtils;

	private final IUserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	@Override
	public String createAuthToken(JwtRequest authRequest) {
		log.info("Starting creating an authentication token: " + authRequest);

		UserDetails userDetails = loadUserByUsername(authRequest.getUsername());
		if (!passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
			throw new AuthenticationFailedException("Password not valid");
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

		final MyUserPrincipal myUserPrincipal = userRepository.findByName(userName)
				.map(MyUserPrincipal::new)
				.orElseThrow(() -> new UserNotFoundException(String.format("Username '%s' not found", userName)));

		log.info("Finalizing user upload by name: " + myUserPrincipal);

		return myUserPrincipal;
	}
}
