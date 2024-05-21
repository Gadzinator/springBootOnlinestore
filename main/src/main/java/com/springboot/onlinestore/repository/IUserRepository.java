package com.springboot.onlinestore.repository;

import com.springboot.onlinestore.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {

	Optional<User> findByName(String name);

	Optional<User> findUserByEmail(String email);
}
