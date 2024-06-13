package com.springboot.onlinestore.service.impl;

import com.springboot.onlinestore.domain.dto.ProductDto;
import com.springboot.onlinestore.domain.entity.Category;
import com.springboot.onlinestore.domain.entity.Order;
import com.springboot.onlinestore.domain.entity.Product;
import com.springboot.onlinestore.domain.entity.WaitingList;
import com.springboot.onlinestore.exception.ProductInUseException;
import com.springboot.onlinestore.exception.ProductNotFoundException;
import com.springboot.onlinestore.mapper.ProductMapper;
import com.springboot.onlinestore.repository.CategoryRepository;
import com.springboot.onlinestore.repository.OrderRepository;
import com.springboot.onlinestore.repository.ProductRepository;
import com.springboot.onlinestore.repository.WaitingListRepository;
import com.springboot.onlinestore.service.ProductService;
import com.springboot.onlinestore.utils.DateConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final WaitingListRepository waitingListRepository;
	private final OrderRepository orderRepository;
	private final ProductMapper productMapper;

	@Transactional
	@Override
	public void save(ProductDto productDto) {
		log.info("Starting adding product: " + productDto);

		if (productDto == null) {
			throw new NullPointerException("ProductDto cannot be null");
		}
		Product product = productMapper.mapToProduct(productDto);
		Category category = findCategoryByName(productDto.getCategory());

		if (category == null) {
			category = addCategory(productDto.getCategory());
		}

		product.setCategory(category);
		productRepository.save(product);
		log.info("Finished adding product: " + productDto);
	}

	@Override
	public ProductDto findById(long id) {
		log.info("Starting finding product by id: " + id);
		final ProductDto productDto = productRepository.findById(id)
				.map(product -> productMapper.mapToProductDto(product))
				.orElseThrow(() -> new ProductNotFoundException("Product not was found by id " + id));
		log.info("Finished finding product by id: " + productDto);

		return productDto;
	}

	@Override
	public Page<ProductDto> findAll(Pageable pageable) {
		log.info("Starting finding all products: " + pageable);
		Page<Product> productsPage = productRepository.findAll(pageable);

		if (productsPage.isEmpty()) {
			final String errorMessage = "Products were not found";
			log.error(errorMessage);
			throw new ProductNotFoundException(errorMessage);
		}
		log.info("Finished finding all products: " + productsPage);

		return productsPage.map(productMapper::mapToProductDto);
	}

	@Override
	public ProductDto findByName(String name) {
		log.info("Starting finding product by name: " + name);
		final ProductDto productDto = productRepository.findByName(name)
				.map(product -> productMapper.mapToProductDto(product))
				.orElseThrow(() -> new ProductNotFoundException("Product not was found by name " + name));

		log.info("Finished finding  product by id: " + productDto);

		return productDto;
	}

	@Override
	public List<ProductDto> findByParams(Map<String, String> params) {
		log.info("Starting finding product by params: " + params);
		List<ProductDto> productDtoList = new ArrayList<>();
		final List<Product> productList = productRepository.findByParams(params);

		if (productList.isEmpty()) {
			final String errorMessage = "Product not was found by params: " + params;
			log.error(errorMessage);
			throw new ProductNotFoundException(errorMessage);
		}

		for (Product product : productList) {
			productDtoList.add(productMapper.mapToProductDto(product));
		}
		log.info("Finished finding product by params: " + params);

		return productDtoList;
	}

	@Transactional
	@Override
	public void update(ProductDto updateProductDto) {
		log.info("Starting Updating product by id: " + updateProductDto.getId());
		Product product = productRepository.findById(updateProductDto.getId())
				.orElseThrow(() -> new ProductNotFoundException("Product not was found by id " + updateProductDto.getId()));
		updateAllFields(product, updateProductDto);
		productRepository.saveAndFlush(product);
		log.info("Finished product updated successfully: " + product);
	}

	@Transactional
	@Override
	public void deleteByID(long id) {
		log.info("Starting deleting product by id: " + id);
		ProductDto productDto = findById(id);
		final Product product = productMapper.mapToProduct(productDto);

		checkIfProductInWaitingList(product);
		checkIfProductInOrder(product);

		productRepository.deleteById(id);
		log.info("Finished Product deleted successfully");
	}

	private Category findCategoryByName(String categoryName) {
		log.info("Finding category by name: " + categoryName);
		return categoryRepository.findByName(categoryName)
				.orElse(null);
	}

	private void updateAllFields(Product product, ProductDto updateProductDto) {
		log.info("Starting update all fields product: " + product);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateConstant.DEFAULT_DATE_PATTERN);
		product.setName(updateProductDto.getName());
		product.setBrand(updateProductDto.getBrand());
		product.setDescription(updateProductDto.getDescription());
		Category category = findCategoryByName(updateProductDto.getCategory());

		if (category == null) {
			category = addCategory(updateProductDto.getCategory());

			log.info("Category not found. Added new category successfully: " + category.getName());
		}
		product.setCategory(category);
		product.setPrice(updateProductDto.getPrice());
		product.setCreated(LocalDate.parse(updateProductDto.getCreated(), dateTimeFormatter));
		product.setAvailable(updateProductDto.isAvailable());
		product.setReceived(LocalDate.parse(updateProductDto.getReceived(), dateTimeFormatter));
		log.info("Finished update all fields product: " + product);
	}

	private Category addCategory(String categoryName) {
		log.info("Starting adding new category with name: " + categoryName);
		Category category = new Category();
		category.setName(categoryName);
		categoryRepository.save(category);
		log.info("Finished new category added successfully: " + category);

		return category;
	}

	private void checkIfProductInWaitingList(Product product) {
		log.info("Starting check if product in waiting list: " + product);
		final Optional<WaitingList> optionalWaitingList = waitingListRepository.findByProduct(product);

		if (optionalWaitingList.isPresent()) {
			final String errorMessage = "The product cannot be deleted it is used in waitingList: " + product;
			log.error(errorMessage);
			throw new ProductInUseException(errorMessage);
		}
		log.info("Finished check if product in waiting list: " + product);
	}

	private void checkIfProductInOrder(Product product) {
		log.info("Starting check if product in order: " + product);
		final List<Order> orderList = orderRepository.findOrdersByProductId(product.getId());

		if (!orderList.isEmpty()) {
			final String errorMessage = "The product cannot be deleted it is used in order: " + orderList;
			log.error(errorMessage);
			throw new ProductInUseException(errorMessage);
		}
		log.info("Finished check if product in order: " + product);
	}
}
