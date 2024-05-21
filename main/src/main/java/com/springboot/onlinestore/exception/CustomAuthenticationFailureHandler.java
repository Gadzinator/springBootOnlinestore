package com.springboot.onlinestore.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Log4j2
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException {
		String errorMessage = "You're not logged in";

		log.error("Authentication failure: {}", exception.getMessage());

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(errorMessage);
	}
}
