package com.springboot.onlinestore.repository;

import com.springboot.onlinestore.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {

	Optional<Product> findByName(String name);
}
