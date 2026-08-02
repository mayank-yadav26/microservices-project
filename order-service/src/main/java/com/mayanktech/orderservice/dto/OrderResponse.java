package com.mayanktech.orderservice.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
	private Long id;
	private String orderNumber;
	private List<OrderLineItemsResponse> orderLineItemsList;
	private BigDecimal totalPrice;
}
