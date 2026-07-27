package com.mayanktech.productservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mayanktech.productservice.dto.ProductRequest;
import com.mayanktech.productservice.dto.ProductResponse;
import com.mayanktech.productservice.modal.Product;
import com.mayanktech.productservice.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	void shouldCreateProduct() {
		ProductRequest request = ProductRequest.builder()
				.name("iPhone 15")
				.description("Apple iPhone 15")
				.price(BigDecimal.valueOf(999.99))
				.build();

		Product savedProduct = Product.builder()
				.id("1")
				.name("iPhone 15")
				.description("Apple iPhone 15")
				.price(BigDecimal.valueOf(999.99))
				.build();

		when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

		productService.createProduct(request);

		verify(productRepository, times(1)).save(any(Product.class));
	}

	@Test
	void shouldReturnAllProducts() {
		Product product1 = Product.builder()
				.id("1")
				.name("iPhone 15")
				.description("Apple iPhone 15")
				.price(BigDecimal.valueOf(999.99))
				.build();

		Product product2 = Product.builder()
				.id("2")
				.name("Samsung Galaxy S24")
				.description("Samsung flagship")
				.price(BigDecimal.valueOf(899.99))
				.build();

		when(productRepository.findAll()).thenReturn(List.of(product1, product2));

		List<ProductResponse> products = productService.getAllProducts();

		assertEquals(2, products.size());
		assertEquals("iPhone 15", products.get(0).getName());
		assertEquals("Samsung Galaxy S24", products.get(1).getName());
		verify(productRepository, times(1)).findAll();
	}

	@Test
	void shouldReturnEmptyListWhenNoProducts() {
		when(productRepository.findAll()).thenReturn(List.of());

		List<ProductResponse> products = productService.getAllProducts();

		assertNotNull(products);
		assertEquals(0, products.size());
	}
}
