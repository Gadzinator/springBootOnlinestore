package com.springboot.onlinestore.domain;

import com.springboot.onlinestore.domain.entity.OrderStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumValidatorImpl implements ConstraintValidator<ValueOfEnum, OrderStatus> {

	private List<String> acceptedValues;

	@Override
	public void initialize(ValueOfEnum annotation) {
		acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
				.map(Enum::name)
				.collect(Collectors.toList());
	}

	@Override
	public boolean isValid(OrderStatus value, ConstraintValidatorContext context) {
		return value != null && acceptedValues.contains(value.toString());
	}
}
