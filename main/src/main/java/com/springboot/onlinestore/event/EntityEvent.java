package com.springboot.onlinestore.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class EntityEvent extends ApplicationEvent {

	private final AccessType accessType;

	public EntityEvent(Object source, AccessType accessType) {
		super(source);
		this.accessType = accessType;
	}
}
