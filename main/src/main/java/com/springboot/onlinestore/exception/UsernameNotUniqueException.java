package com.springboot.onlinestore.exception;

public class UsernameNotUniqueException extends RuntimeException {

	public UsernameNotUniqueException(String message) {
		super(message);
	}
}
