package com.springboot.onlinestore.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;
import java.util.StringJoiner;

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name", length = 30, nullable = false, unique = true)
	private String name;

	@Column(name = "email", length = 30, nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Order> orders;

	public User() {
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
				.add("id=" + id)
				.add("name='" + name + "'")
				.add("email='" + email + "'")
				.add("role=" + role)
				.toString();
	}
}
