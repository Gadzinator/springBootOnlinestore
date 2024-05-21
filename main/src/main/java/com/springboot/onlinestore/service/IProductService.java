package com.springboot.onlinestore.service;

import com.springboot.onlinestore.domain.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IProductService {

	void save(ProductDto productDto);

	ProductDto findById(long id);

	Page<ProductDto> findAll(Pageable pageable);

	ProductDto findByName(String name);

	List<ProductDto> findByParams(Map<String, String> params);

	void update(ProductDto updateProductDto);

	void deleteByID(long id);
}
