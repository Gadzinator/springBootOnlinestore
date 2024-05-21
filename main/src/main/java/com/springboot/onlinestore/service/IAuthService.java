package com.springboot.onlinestore.service;

import com.springboot.onlinestore.domain.dto.JwtRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IAuthService extends UserDetailsService {

	String createAuthToken(JwtRequest authRequest);

	UserDetails loadUserByUsername(String userName);
}
