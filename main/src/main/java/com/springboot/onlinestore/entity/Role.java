package com.springboot.onlinestore.entity;

import lombok.Getter;

@Getter
public enum Role {

	ROLE_USER("ROLE_USER"), ROLE_ADMIN("ROLE_ADMIN");

	private final String value;

	Role(String value) {
		this.value = value;
	}
}
