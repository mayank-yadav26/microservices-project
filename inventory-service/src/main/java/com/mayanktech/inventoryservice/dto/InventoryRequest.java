package com.mayanktech.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryRequest {

	@NotBlank(message = "skuCode cannot be blank")
	private String skuCode;

	@NotNull(message = "quantity cannot be null")
	@Min(value = 0, message = "quantity cannot be negative")
	private Integer quantity;
}
