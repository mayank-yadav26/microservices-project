package com.mayanktech.productservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mayanktech.productservice.dto.ProductRequest;
import com.mayanktech.productservice.dto.ProductResponse;
import com.mayanktech.productservice.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void createProduct(@RequestBody @Valid ProductRequest productRequest) {
		productService.createProduct(productRequest);
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ProductResponse> getAllProducts(){
		return productService.getAllProducts();
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ProductResponse getProduct(@PathVariable String id) {
		return productService.getProduct(id);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void updateProduct(@PathVariable String id, @RequestBody @Valid ProductRequest productRequest) {
		productService.updateProduct(id, productRequest);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProduct(@PathVariable String id) {
		productService.deleteProduct(id);
	}
}
