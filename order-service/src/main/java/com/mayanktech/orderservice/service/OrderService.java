package com.mayanktech.orderservice.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.mayanktech.common.dto.InventoryAdjustmentRequest;
import com.mayanktech.common.dto.InventoryResponse;
import com.mayanktech.common.event.OrderPlacedEvent;
import com.mayanktech.orderservice.dto.OrderLineItemsDto;
import com.mayanktech.orderservice.dto.OrderLineItemsResponse;
import com.mayanktech.orderservice.dto.OrderRequest;
import com.mayanktech.orderservice.dto.OrderResponse;
import com.mayanktech.orderservice.model.Order;
import com.mayanktech.orderservice.model.OrderLineItems;
import com.mayanktech.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

	private final OrderRepository orderRepository;
	private final WebClient.Builder webClientBuilder;
	private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

	@Transactional
	public String placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());

		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToDto)
				.toList();

		order.setOrderLineItemsList(orderLineItems);

		List<InventoryAdjustmentRequest> adjustments = order.getOrderLineItemsList().stream()
				.map(item -> InventoryAdjustmentRequest.builder().skuCode(item.getSkuCode())
						.quantity(item.getQuantity()).build())
				.toList();

		// atomically decrease inventory and place order only if stock is sufficient
		InventoryResponse[] inventoryResponseArray = webClientBuilder.build().post()
				.uri("http://inventory-service/api/inventory/decrease")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(adjustments)
				.retrieve().bodyToMono(InventoryResponse[].class).block();

		boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
				.allMatch(InventoryResponse::getIsInStock);

		if (allProductsInStock) {
			orderRepository.save(order);
			kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()))
					.whenComplete((result, ex) -> {
						if (ex != null) {
							log.error("Failed to send OrderPlacedEvent for order {}: {}",
									order.getOrderNumber(), ex.getMessage());
						} else {
							log.info("Successfully sent OrderPlacedEvent for order {}",
									order.getOrderNumber());
						}
					});
			return "Order Placed Successfully!";
		} else {
			return "Product is not in stock, please try again later.";
		}
	}

	@Transactional(readOnly = true)
	public List<OrderResponse> listOrders() {
		return orderRepository.findAll().stream().map(this::mapToOrderResponse).toList();
	}

	private OrderResponse mapToOrderResponse(Order order) {
		List<OrderLineItemsResponse> items = order.getOrderLineItemsList().stream().map(item -> OrderLineItemsResponse
				.builder().skuCode(item.getSkuCode()).price(item.getPrice()).quantity(item.getQuantity()).build())
				.toList();
		BigDecimal totalPrice = items.stream().map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return OrderResponse.builder().id(order.getId()).orderNumber(order.getOrderNumber())
				.orderLineItemsList(items).totalPrice(totalPrice).build();
	}

	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}

}
