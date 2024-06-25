package com.springboot.onlinestore.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AuthEvent extends ApplicationEvent {

	private final String username;
	private final AuthAction authAction;

	public AuthEvent(Object source, String username, AuthAction authAction) {
		super(source);
		this.username = username;
		this.authAction = authAction;
	}
}
