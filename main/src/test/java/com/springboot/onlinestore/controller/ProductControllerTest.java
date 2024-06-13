package com.springboot.onlinestore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.onlinestore.domain.dto.CategoryDto;
import com.springboot.onlinestore.domain.dto.ProductDto;
import com.springboot.onlinestore.exception.ProductNotFoundException;
import com.springboot.onlinestore.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerTest {

	private static final String PRODUCT_NAME = "Toy name";

	private static final long PRODUCT_ID = 1;

	private static final long NOT_FOUND_PRODUCT_ID = 10;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductService productService;

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).apply((SecurityMockMvcConfigurers.springSecurity())).build();
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void testSaveWhenHttpStatusOk() throws Exception {
		final ProductDto productDto = createProductDto();

		mockMvc.perform(post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productDto)))
				.andExpect(status().isCreated());
	}

	@Test
	@WithAnonymousUser
	public void testSaveWhenHttpIsUnauthorized() throws Exception {
		final ProductDto productDto = createProductDto();

		mockMvc.perform(post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productDto)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void testSaveWhenHttPStatusBadRequest() throws Exception {
		mockMvc.perform(post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(null)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testFindAllWhenHttpStatusOk() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.save(productDto);

		mockMvc.perform(get("/products/all"))
				.andExpect(status().isOk());
	}

	@Test
	public void testFindByIdWhenStatusProductNotFoundException() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.save(productDto);

		mockMvc.perform(get("/products/{id}", NOT_FOUND_PRODUCT_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Product not was found by id " + NOT_FOUND_PRODUCT_ID,
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	public void testFindByIdWhenStatusOk() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.save(productDto);

		mockMvc.perform(get("/products/{id}", PRODUCT_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productDto)))
				.andExpect(status().isOk());
	}

	@Test
	public void testFindByNameWhenHttpStatusOk() throws Exception {
		final ProductDto productDto = createProductDto();
		productDto.setName("Product");
		productService.save(productDto);

		mockMvc.perform(get("/products/name")
						.param("name", "Product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productDto)))
				.andExpect(status().isOk());
	}

	@Test
	public void testFindByParamsWhenHttpStatusOk() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.save(productDto);

		mockMvc.perform(get("/products")
						.param("brand", "Toy brand")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testFindByParamsWhenHttpStatusNotFound() throws Exception {
		mockMvc.perform(get("/products")
						.param("brand", "Toy brand")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void testUpdateWhenHttpStatusOk() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.save(productDto);
		productDto.setId(PRODUCT_ID);

		mockMvc.perform(put("/products", productDto)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productDto)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void testUpdateWhenProductNotFoundException() throws Exception {
		final ProductDto productDto = createProductDto();
		productDto.setId(100);

		mockMvc.perform(put("/products", productDto)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productDto)))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Product not was found by id " + 100,
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void testDeleteByIdWhenHttpStatusIsAccepted() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.save(productDto);

		mockMvc.perform(delete("/products/{id}", PRODUCT_ID))
				.andExpect(status().isAccepted());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void testDeleteByIdWhenProductNotFoundException() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.save(productDto);

		mockMvc.perform(delete("/products/{id}", NOT_FOUND_PRODUCT_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertInstanceOf(ProductNotFoundException.class, result.getResolvedException()))
				.andExpect(result -> assertEquals("Product not was found by id " + NOT_FOUND_PRODUCT_ID,
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	private ProductDto createProductDto() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(1);
		ProductDto productDto = new ProductDto();
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
