package com.mayanktech.productservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductRequest {
	@NotBlank(message = "Product name cannot be blank")
	private String name;
	private String description;
	@NotNull(message = "Product price cannot be null")
	@Positive(message = "Product price must be positive")
	private BigDecimal price;
}
