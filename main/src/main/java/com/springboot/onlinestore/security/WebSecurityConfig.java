package com.springboot.onlinestore.security;

import com.springboot.onlinestore.exception.CustomAccessDeniedHandler;
import com.springboot.onlinestore.exception.CustomAuthenticationEntryPoint;
import com.springboot.onlinestore.exception.CustomAuthenticationFailureHandler;
import com.springboot.onlinestore.exception.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
@Import(AuthenticationProvider.class)
public class WebSecurityConfig {

	private final String ROLE_ADMIN = "ADMIN";
	private final JwtRequestFilter requestFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.cors(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.POST, "/products/**").hasRole(ROLE_ADMIN)
						.requestMatchers("/users/**").hasRole(ROLE_ADMIN)
						.requestMatchers(HttpMethod.DELETE, "/**").hasRole(ROLE_ADMIN)
						.requestMatchers(HttpMethod.PUT, "/**").hasRole(ROLE_ADMIN)
						.requestMatchers(HttpMethod.GET, "/orders/**", "/waitingLists/{id}").authenticated()
						.requestMatchers(HttpMethod.GET, "/waitingLists/**").hasRole(ROLE_ADMIN)
						.requestMatchers("/admins/**").hasRole((ROLE_ADMIN))
						.requestMatchers(HttpMethod.POST, "/waitingLists/**", "/orders/**", "/changePassword").authenticated()
						.anyRequest().permitAll())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.formLogin(form -> form.failureHandler(authenticationFailureHandler()))
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.accessDeniedHandler(accessDeniedHandler())
						.authenticationEntryPoint(authenticationEntryPoint()))
				.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
		try {
			return configuration.getAuthenticationManager();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}
}
