package com.springboot.onlinestore.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name", nullable = false, length = 30)
	private String name;

	@Column(name = "brand", nullable = false, length = 30)
	private String brand;

	@Column(name = "description",nullable = false)
	private String description;

	@ManyToOne(cascade = CascadeType.PERSIST, optional = false)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@Column(name = "price", nullable = false)
	private int price;

	@Column(name = "created", nullable = false)
	private LocalDate created;

	@Column(name = "is_available", nullable = false)
	private boolean isAvailable;

	@Column(name = "received", nullable = false)
	private LocalDate received;

	@Override
	public String toString() {
		return "Product{" +
				"id=" + id +
				", name='" + name + '\'' +
				", brand='" + brand + '\'' +
				", description='" + description + '\'' +
				", category=" + (category != null ? category.getName() : "null") +
				", price=" + price +
				", created=" + created +
				", isAvailable=" + isAvailable +
				", received=" + received +
				'}';
	}
}
