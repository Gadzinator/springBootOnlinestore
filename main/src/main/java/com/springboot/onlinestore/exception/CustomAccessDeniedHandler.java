package com.springboot.onlinestore.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Log4j2
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String message = "Access denied";
		if (auth != null) {
			message += ", " + auth.getName() + " does not have sufficient access rights";
			log.warn("User: " + auth.getName() + " attempted to access the protected URL: " + request.getRequestURI());
		} else {
			message += ", Anonymous user does not have sufficient access rights";
			log.warn("Anonymous user attempted to access the protected URL: " + request.getRequestURI());
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getWriter().write(message);
	}
}
