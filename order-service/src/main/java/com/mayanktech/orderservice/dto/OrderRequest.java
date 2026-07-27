package com.mayanktech.orderservice.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
	@NotEmpty(message = "Order must have at least one line item")
	@Valid
	private List<OrderLineItemsDto> orderLineItemsDtoList;
}
