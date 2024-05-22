package com.springboot.onlinestore.repository;

import com.springboot.onlinestore.domain.entity.Category;
import com.springboot.onlinestore.domain.entity.Product;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class IProductRepositoryTest {

	@Resource
	private IProductRepository productRepository;

	private static final String UPDATE_PRODUCT_NAME = "Updated Product Name";

	private static final String PRODUCT_NAME = "Toy name";

	@Test
	public void testSave() {
		// given
		final Product product = createProduct();

		// when
		productRepository.save(product);

		// then
		final Optional<Product> optionalProduct = productRepository.findById(product.getId());

		assertTrue(optionalProduct.isPresent());
		optionalProduct.ifPresent(p -> {
			assertEquals(product.getId(), p.getId());
			assertEquals(product.getName(), p.getName());
		});
	}

	@Test
	public void testFindById() {
		// given
		Product product = createProduct();
		productRepository.save(product);

		// when
		final Optional<Product> optionalProduct = productRepository.findById(product.getId());

		// then
		assertTrue(optionalProduct.isPresent());
		optionalProduct.ifPresent(p -> {
			assertEquals(product.getId(), p.getId());
			assertEquals(product.getName(), p.getName());
		});
	}

	@Test
	public void testUpdate() {
		// given
		Product product = createProduct();
		productRepository.save(product);

		product.setName(UPDATE_PRODUCT_NAME);

		// when
		productRepository.saveAndFlush(product);

		// then
		final Optional<Product> optionalProduct = productRepository.findById(product.getId());

		assertTrue(optionalProduct.isPresent());
		optionalProduct.ifPresent(p ->
				assertEquals(UPDATE_PRODUCT_NAME, p.getName()));
	}

	@Test
	public void testFindByName() {
		// given
		Product product = createProduct();
		productRepository.save(product);

		// when
		final Optional<Product> optionalProduct = productRepository.findByName(PRODUCT_NAME);

		// then
		assertNotNull(optionalProduct);
		optionalProduct.ifPresent(p ->
				assertEquals(PRODUCT_NAME, p.getName()));
	}

	@Test
	public void testDeleteByID() {
		// given
		Product product = createProduct();
		productRepository.save(product);

		// when
		productRepository.deleteById(product.getId());

		// then
		final Optional<Product> optionalProduct = productRepository.findById(product.getId());
		assertTrue(optionalProduct.isEmpty());
	}

	private Product createProduct() {
		Category category = new Category();
		category.setName("CLOTHES");
		Product product = new Product();
		product.setName(PRODUCT_NAME);
		product.setBrand("Toy brand");
		product.setDescription("Toy description");
		product.setCategory(category);
		product.setPrice(100);
		product.setCreated(LocalDate.parse("2023-10-10"));
		product.setAvailable(true);
		product.setReceived(LocalDate.parse("2023-11-10"));

		return product;
	}
}
