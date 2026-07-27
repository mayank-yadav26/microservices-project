package com.mayanktech.orderservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItemsDto {
	private Long id;
	@NotBlank(message = "SKU code cannot be blank")
	private String skuCode;
	@NotNull(message = "Price cannot be null")
	@Positive(message = "Price must be positive")
	private BigDecimal price;
	@NotNull(message = "Quantity cannot be null")
	@Positive(message = "Quantity must be positive")
	private Integer quantity;
}
