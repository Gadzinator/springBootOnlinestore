package com.springboot.onlinestore.listiner;

import com.springboot.onlinestore.event.EntityEvent;
import com.springboot.onlinestore.utils.ConsoleUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EntityListener {

	@EventListener
	public void acceptEntity(EntityEvent event) {
		ConsoleUtils.print(event + ", " + event.getAccessType());
	}
}
