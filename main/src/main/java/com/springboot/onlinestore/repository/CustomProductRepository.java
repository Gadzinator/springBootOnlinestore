package com.springboot.onlinestore.repository;

import com.springboot.onlinestore.domain.entity.Product;

import java.util.List;
import java.util.Map;

public interface CustomProductRepository {

	List<Product> findByParams(Map<String, String> params);
}
