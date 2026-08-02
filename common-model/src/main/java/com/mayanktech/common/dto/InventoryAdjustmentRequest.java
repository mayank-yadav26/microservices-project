package com.mayanktech.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryAdjustmentRequest {
	private String skuCode;
	private Integer quantity;
}
