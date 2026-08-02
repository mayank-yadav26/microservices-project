package com.mayanktech.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mayanktech.productservice.dto.ProductRequest;
import com.mayanktech.productservice.dto.ProductResponse;
import com.mayanktech.productservice.exception.ResourceNotFoundException;
import com.mayanktech.productservice.model.Product;
import com.mayanktech.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public void createProduct(ProductRequest productRequest) {
		Product product = Product.builder().name(productRequest.getName()).description(productRequest.getDescription())
				.price(productRequest.getPrice()).build();
		productRepository.save(product);

		log.info("Product {} is saved", product.getId());
	}

	public List<ProductResponse> getAllProducts() {
		List<Product> products = productRepository.findAll();
		return products.stream().map(this::mapToProductResponse).toList();
	}

	public ProductResponse getProduct(String id) {
		Product product = findProductById(id);
		return mapToProductResponse(product);
	}

	public void updateProduct(String id, ProductRequest productRequest) {
		Product product = findProductById(id);
		product.setName(productRequest.getName());
		product.setDescription(productRequest.getDescription());
		product.setPrice(productRequest.getPrice());
		productRepository.save(product);
		log.info("Product {} is updated", product.getId());
	}

	public void deleteProduct(String id) {
		findProductById(id);
		productRepository.deleteById(id);
		log.info("Product {} is deleted", id);
	}

	private Product findProductById(String id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
	}

	private ProductResponse mapToProductResponse(Product product) {
		return ProductResponse.builder().id(product.getId()).name(product.getName())
				.description(product.getDescription()).price(product.getPrice()).build();
	}
}
