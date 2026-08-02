package com.mayanktech.orderservice.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mayanktech.orderservice.dto.OrderRequest;
import com.mayanktech.orderservice.dto.OrderResponse;
import com.mayanktech.orderservice.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
	@TimeLimiter(name = "inventory")
	@Retry(name = "inventory")
	public CompletableFuture<String> placeOrder(@RequestBody @Valid OrderRequest orderRequest) {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		return CompletableFuture.supplyAsync(() -> {
			Thread.currentThread().setContextClassLoader(contextClassLoader);
			return orderService.placeOrder(orderRequest);
		});
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<OrderResponse> getOrders() {
		return orderService.listOrders();
	}

	public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
		return CompletableFuture.supplyAsync(()->"Oops! something went wrong please order after sometime!");
	}
	
}
