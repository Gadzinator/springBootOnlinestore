package com.springboot.onlinestore.repository;

import com.springboot.onlinestore.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String name);
}
