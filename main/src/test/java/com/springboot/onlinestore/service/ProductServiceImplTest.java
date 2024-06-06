package com.springboot.onlinestore.service;

import com.springboot.onlinestore.domain.dto.CategoryDto;
import com.springboot.onlinestore.domain.dto.ProductDto;
import com.springboot.onlinestore.domain.entity.Category;
import com.springboot.onlinestore.domain.entity.Product;
import com.springboot.onlinestore.exception.ProductNotFoundException;
import com.springboot.onlinestore.mapper.ProductMapperImpl;
import com.springboot.onlinestore.repository.CategoryRepository;
import com.springboot.onlinestore.repository.OrderRepository;
import com.springboot.onlinestore.repository.ProductRepository;
import com.springboot.onlinestore.repository.WaitingListRepository;
import com.springboot.onlinestore.service.impl.ProductServiceImpl;
import com.springboot.onlinestore.utils.DateConstant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

	private static final String PRODUCT_NAME = "Toy name";

	private static final long PRODUCT_ID = 1;

	private static final int PAGE_NUMBER = 0;

	private static final int PAGE_SIZE = 10;

	@Mock
	private ProductRepository productRepository;

	@Spy
	private ProductMapperImpl productMapper;

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private WaitingListRepository waitingListRepository;

	@InjectMocks
	private ProductServiceImpl productServiceImpl;

	@Test
	public void testSave() {
		// given
		Category category = new Category();
		category.setName("TOY");
		ProductDto productDto = createProductDto();
		final Product product = createProduct();

		when(productMapper.mapToProduct(any(ProductDto.class))).thenReturn(product);
		when(categoryRepository.findByName(productDto.getCategory())).thenReturn(Optional.of(category));
		when(productRepository.save(any(Product.class))).thenReturn(product);

		// when
		productServiceImpl.save(productDto);

		// then
		verify(productMapper).mapToProduct(productDto);
		verify(categoryRepository).findByName(productDto.getCategory());
		verify(productRepository).save(product);
		assertEquals(product.getId(), productDto.getId());
		assertEquals(product.getName(), productDto.getName());
	}

	@Test
	public void testSaveProductWhenNull() {
		// when and then
		assertThrows(NullPointerException.class, () -> productServiceImpl.save(null));
	}

	@Test
	public void testFindByIdWhenProductExist() {
		// given
		final Product product = createProduct();
		final ProductDto expectedProductDto = createProductDto();

		when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
		when(productMapper.mapToProductDto(product)).thenReturn(expectedProductDto);

		// when
		ProductDto actualProductDto = productServiceImpl.findById(PRODUCT_ID);

		// then
		verify(productRepository).findById(PRODUCT_ID);
		verify(productMapper).mapToProductDto(product);

		assertEquals(expectedProductDto, actualProductDto);
	}

	@Test
	public void testFindByIdWhenProductNotExist() {
		// given
		when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

		// when
		assertThrows(ProductNotFoundException.class, () -> productServiceImpl.findById(PRODUCT_ID));

		// then
		verify(productRepository).findById(PRODUCT_ID);
	}

	@Test
	public void testFindAllListNotEmpty() {
		// given
		final Product firstProduct = createProduct();
		final Product secondProduct = createProduct();
		secondProduct.setId(2);
		final List<Product> products = Arrays.asList(firstProduct, secondProduct);

		final ProductDto firstProductDto = createProductDto();
		final ProductDto secondProductDto = createProductDto();
		secondProductDto.setId(2);

		when(productRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(products));
		when(productMapper.mapToProductDto(firstProduct)).thenReturn(firstProductDto);
		when(productMapper.mapToProductDto(secondProduct)).thenReturn(secondProductDto);

		final Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

		// when
		final Page<ProductDto> actualProductDtoList = productServiceImpl.findAll(pageable);

		// then
		verify(productRepository).findAll(any(Pageable.class));
		verify(productMapper).mapToProductDto(firstProduct);
		verify(productMapper).mapToProductDto(secondProduct);

		assertFalse(actualProductDtoList.isEmpty());
		assertEquals(2, actualProductDtoList.getSize());
		assertTrue(actualProductDtoList.getContent().contains(firstProductDto));
		assertTrue(actualProductDtoList.getContent().contains(secondProductDto));
	}

	@Test
	public void testFindAllWhenProductsNotExist() {
		// given
		when(productRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

		final Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

		// when
		assertThrows(ProductNotFoundException.class, () -> productServiceImpl.findAll(pageable));

		// then
		verify(productRepository).findAll(pageable);
	}

	@Test
	public void findByNameProductExist() {
		// given
		final Product product = createProduct();
		final ProductDto productDto = createProductDto();

		when(productRepository.findByName(PRODUCT_NAME)).thenReturn(Optional.of(product));
		when(productMapper.mapToProductDto(product)).thenReturn(productDto);

		// when
		final ProductDto actualeProductDto = productServiceImpl.findByName(PRODUCT_NAME);

		// then
		verify(productRepository, times(1)).findByName(PRODUCT_NAME);
		verify(productMapper, times(1)).mapToProductDto(product);

		assertEquals(productDto.getId(), actualeProductDto.getId());
		assertEquals(productDto.getName(), actualeProductDto.getName());
		assertEquals(productDto.getBrand(), actualeProductDto.getBrand());
	}

	@Test
	public void findByNameWhenProductNotExist() {
		// given
		when(productRepository.findByName(PRODUCT_NAME)).thenReturn(Optional.empty());

		// when
		assertThrows(ProductNotFoundException.class, () -> productServiceImpl.findByName(PRODUCT_NAME));

		// then
		verify(productRepository).findByName(PRODUCT_NAME);
	}

	@Test
	public void findByParamsWhenProductExist() {
		// given
		Map<String, String> params = new HashMap<>();
		params.put("brand", "Toy brand");

		final Product product = createProduct();
		final ProductDto productDto = createProductDto();

		when(productRepository.findByParams(params)).thenReturn(List.of(product));
		when(productMapper.mapToProductDto(product)).thenReturn(productDto);

		// when
		final List<ProductDto> productDtoList = productServiceImpl.findByParams(params);

		// then
		verify(productRepository).findByParams(params);
		verify(productMapper).mapToProductDto(product);

		assertEquals(1, productDtoList.size());
		assertEquals(productDto, productDtoList.get(0));
	}

	@Test
	public void findByParamsWhenProductNotExist() {
		// given
		Map<String, String> params = new HashMap<>();
		params.put("brand", "Brand");

		when(productRepository.findByParams(params)).thenReturn(Collections.emptyList());

		// when
		assertThrows(ProductNotFoundException.class, () -> productServiceImpl.findByParams(params));

		// then
		verify(productRepository).findByParams(params);
	}

	@Test
	public void testUpdateWhenProductFound() {
		// given
		final ProductDto productDto = createProductDto();
		productDto.setName("Product Dto");
		Category category = new Category();
		category.setName("TOY");
		final Product product = createProduct();
		product.setName("Product Dto");

		when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
		when(categoryRepository.findByName(productDto.getCategory())).thenReturn(Optional.of(category));
		when(productRepository.saveAndFlush(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// when
		productServiceImpl.update(productDto);

		// then
		final ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

		verify(productRepository).findById(PRODUCT_ID);
		verify(categoryRepository).findByName(productDto.getCategory());
		verify(productRepository).saveAndFlush(productArgumentCaptor.capture());
		final Product savedProduct = productArgumentCaptor.getValue();

		assertEquals("Product Dto", savedProduct.getName());
	}

	@Test
	public void testUpdateWhenProductNotFound() {
		// given
		final ProductDto productDto = createProductDto();

		when(productRepository.findById(productDto.getId())).thenReturn(Optional.empty());

		// when
		assertThrows(ProductNotFoundException.class, () -> productServiceImpl.update(productDto));

		// then
		verify(productRepository).findById(productDto.getId());
		verify(productRepository, never()).saveAndFlush(any(Product.class));
	}

	@Test
	public void deleteByIDWhenProductExist() {
		// given
		final Product product = createProduct();

		final ProductDto expectedProductDto = createProductDto();

		when(waitingListRepository.findByProduct(product)).thenReturn(Optional.empty());
		when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
		when(productMapper.mapToProductDto(product)).thenReturn(expectedProductDto);
		when(waitingListRepository.findByProduct(product)).thenReturn(Optional.empty());
		when(orderRepository.findOrdersByProductId(PRODUCT_ID)).thenReturn(Collections.emptyList());

		// when
		productServiceImpl.deleteByID(PRODUCT_ID);

		// then
		verify(waitingListRepository).findByProduct(product);
		verify(productRepository).findById(PRODUCT_ID);
		verify(productMapper).mapToProductDto(product);
		verify(waitingListRepository).findByProduct(product);
		verify(orderRepository).findOrdersByProductId(PRODUCT_ID);
	}

	@Test
	public void deleteByIDWhenProductNotExist() {
		// given
		when(productRepository.findById(PRODUCT_ID)).thenThrow(ProductNotFoundException.class);

		// when
		assertThrows(ProductNotFoundException.class, () -> productServiceImpl.deleteByID(PRODUCT_ID));

		// then
		verify(productRepository, never()).deleteById(PRODUCT_ID);
	}

	private Product createProduct() {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateConstant.DEFAULT_DATE_PATTERN);
		Category category = new Category();
		category.setName("TOY");
		Product product = new Product();
		product.setId(PRODUCT_ID);
		product.setName(PRODUCT_NAME);
		product.setBrand("Toy brand");
		product.setDescription("Toy description");
		product.setCategory(category);
		product.setPrice(100);
		product.setCreated(LocalDate.parse("01-11-2024", dateTimeFormatter));
		product.setAvailable(true);
		product.setReceived(LocalDate.parse("01-01-2024", dateTimeFormatter));

		return product;
	}

	private ProductDto createProductDto() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(1);
		categoryDto.setName("TOY");
		ProductDto productDto = new ProductDto();
		productDto.setId(PRODUCT_ID);
		productDto.setName(PRODUCT_NAME);
		productDto.setBrand("Toy brand");
		productDto.setDescription("Toy description");
		productDto.setCategory("TOY");
		productDto.setPrice(100);
		productDto.setCreated("01-11-2024");
		productDto.setAvailable(true);
		productDto.setReceived("01-01-2024");

		return productDto;
	}
}
