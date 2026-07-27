package com.mayanktech.orderservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.mayanktech.common.dto.InventoryResponse;
import com.mayanktech.common.event.OrderPlacedEvent;
import com.mayanktech.orderservice.dto.OrderLineItemsDto;
import com.mayanktech.orderservice.dto.OrderRequest;
import com.mayanktech.orderservice.repository.OrderRepository;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private WebClient webClient;

	@Mock
	private WebClient.Builder webClientBuilder;

	@Mock
	private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

	@InjectMocks
	private OrderService orderService;

	@SuppressWarnings("unchecked")
	private void mockInventoryCheck(InventoryResponse[] responseArray) {
		WebClient.RequestHeadersUriSpec<?> getSpec = org.mockito.Mockito.mock(WebClient.RequestHeadersUriSpec.class);
		WebClient.ResponseSpec responseSpec = org.mockito.Mockito.mock(WebClient.ResponseSpec.class);

		when(webClientBuilder.build()).thenReturn(webClient);
		when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) getSpec);
		org.mockito.Mockito.doReturn(getSpec).when((WebClient.RequestHeadersUriSpec) getSpec)
				.uri(anyString(), any(Function.class));
		when(getSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.bodyToMono(InventoryResponse[].class))
				.thenReturn(Mono.just(responseArray));
	}

	@Test
	void shouldPlaceOrderSuccessfully() {
		OrderLineItemsDto lineItem = new OrderLineItemsDto();
		lineItem.setSkuCode("iphone-15");
		lineItem.setPrice(BigDecimal.valueOf(999.99));
		lineItem.setQuantity(1);

		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderLineItemsDtoList(List.of(lineItem));

		InventoryResponse inventoryResponse = InventoryResponse.builder()
				.skuCode("iphone-15")
				.isInStock(true)
				.build();

		mockInventoryCheck(new InventoryResponse[]{inventoryResponse});
		when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
		when(kafkaTemplate.send(anyString(), any(OrderPlacedEvent.class))).thenReturn(null);

		String result = orderService.placeOrder(orderRequest);

		assertEquals("Order Placed Successfully!", result);
		verify(orderRepository, times(1)).save(any());
		verify(kafkaTemplate, times(1)).send(eq("notificationTopic"), any(OrderPlacedEvent.class));
	}

	@Test
	void shouldNotPlaceOrderWhenOutOfStock() {
		OrderLineItemsDto lineItem = new OrderLineItemsDto();
		lineItem.setSkuCode("iphone-12");
		lineItem.setPrice(BigDecimal.valueOf(699.99));
		lineItem.setQuantity(1);

		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderLineItemsDtoList(List.of(lineItem));

		InventoryResponse inventoryResponse = InventoryResponse.builder()
				.skuCode("iphone-12")
				.isInStock(false)
				.build();

		mockInventoryCheck(new InventoryResponse[]{inventoryResponse});

		String result = orderService.placeOrder(orderRequest);

		assertEquals("Product is not in stock, please try again later.", result);
		verify(orderRepository, never()).save(any());
		verify(kafkaTemplate, never()).send(anyString(), any());
	}
}
