package com.springboot.onlinestore.listiner;

import com.springboot.onlinestore.event.AuthEvent;
import com.springboot.onlinestore.utils.ConsoleUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AuthEventListener {

	@EventListener
	public void handleAuthEvent(AuthEvent event) {
		ConsoleUtils.print("Authentication event: " + event.getUsername() + ", Action: " + event.getAuthAction());
	}
}
