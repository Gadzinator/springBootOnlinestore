package com.springboot.onlinestore.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum OrderStatus {

	READY("READY"), NOT_READY("NOT_READY"), IN_PROGRESS("IN_PROGRESS");

	private final String value;

	OrderStatus(String value) {
		this.value = value;
	}
}
