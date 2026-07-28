package com.mayanktech.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.mayanktech.common.dto.InventoryResponse;
import com.mayanktech.common.event.OrderPlacedEvent;
import com.mayanktech.orderservice.dto.OrderLineItemsDto;
import com.mayanktech.orderservice.dto.OrderRequest;
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

		List<String> skuCodesList = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

		// call inventory service ,and place order if product is in stock
		InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
				.uri("http://inventory-service/api/inventory",
						uriBuilder -> uriBuilder.queryParam("skuCodeList", skuCodesList).build())
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

	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}

}
