package com.springboot.onlinestore.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Log4j2
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		String message = "Unauthorized";
		String username = request.getRemoteUser();
		if (username != null && !username.isEmpty()) {
			message += ", " + username + " is not authenticated";

			log.warn(message + "url" + request.getRequestURI());
		} else {
			message += ", Anonymous user is not authenticated";

			log.warn(message + ", url" + request.getRequestURI());
		}

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(message);
	}
}
