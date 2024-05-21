package com.springboot.onlinestore.exception;

public class ProductInUseException extends RuntimeException {

	public ProductInUseException(String message) {
		super(message);
	}
}
