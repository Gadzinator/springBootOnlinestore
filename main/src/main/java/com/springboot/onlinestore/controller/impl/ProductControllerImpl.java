package com.springboot.onlinestore.controller.impl;

import com.springboot.onlinestore.controller.ProductController;
import com.springboot.onlinestore.domain.dto.ProductDto;
import com.springboot.onlinestore.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductControllerImpl implements ProductController {

	private final ProductService productService;

	@Override
	@PostMapping
	public ResponseEntity<?> save(@RequestBody(required = false) ProductDto productDto) {
		if (productDto == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		productService.save(productDto);

		return new ResponseEntity<>("Product add", HttpStatus.CREATED);
	}

	@Override
	@GetMapping("/all")
	public ResponseEntity<List<ProductDto>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
													@RequestParam(value = "size", defaultValue = "10") int size) {
		final Pageable pageable = PageRequest.of(page, size);
		Page<ProductDto> productsPage = productService.findAll(pageable);

		return new ResponseEntity<>(productsPage.getContent(), HttpStatus.OK);
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable(value = "id") Long id) {
		ProductDto productDto = productService.findById(id);

		return new ResponseEntity<>(productDto, HttpStatus.OK);
	}

	@Override
	@GetMapping("/name")
	public ResponseEntity<?> findByName(@RequestParam(value = "name") String name) {
		return new ResponseEntity<>(productService.findByName(name), HttpStatus.OK);
	}

	@Override
	@GetMapping
	public ResponseEntity<List<ProductDto>> findByParams(@RequestParam(required = false) Map<String, String> params) {
		List<ProductDto> productDtoList = productService.findByParams(params);

		if (productDtoList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(productDtoList, HttpStatus.OK);
	}

	@Override
	@PutMapping
	public ResponseEntity<?> update(@RequestBody ProductDto updateProductDto) {
		productService.update(updateProductDto);

		return new ResponseEntity<>(updateProductDto, HttpStatus.OK);
	}

	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteByID(@PathVariable("id") Long id) {
		productService.deleteByID(id);

		return new ResponseEntity<>("The product has been removed", HttpStatus.ACCEPTED);
	}
}
