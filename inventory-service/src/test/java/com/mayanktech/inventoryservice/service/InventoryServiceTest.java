package com.mayanktech.inventoryservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mayanktech.common.dto.InventoryResponse;
import com.mayanktech.inventoryservice.modal.Inventory;
import com.mayanktech.inventoryservice.repository.InventoryRepository;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

	@Mock
	private InventoryRepository inventoryRepository;

	@InjectMocks
	private InventoryService inventoryService;

	@Test
	void shouldReturnInStockForSufficientQuantity() {
		Inventory inventory = new Inventory();
		inventory.setSkuCode("iphone-15");
		inventory.setQuantity(100);

		when(inventoryRepository.findBySkuCodeIn(List.of("iphone-15")))
				.thenReturn(List.of(inventory));

		List<InventoryResponse> result = inventoryService.isInStock(List.of("iphone-15"));

		assertEquals(1, result.size());
		assertTrue(result.get(0).getIsInStock());
		assertEquals("iphone-15", result.get(0).getSkuCode());
	}

	@Test
	void shouldReturnNotInStockForZeroQuantity() {
		Inventory inventory = new Inventory();
		inventory.setSkuCode("iphone-12");
		inventory.setQuantity(0);

		when(inventoryRepository.findBySkuCodeIn(List.of("iphone-12")))
				.thenReturn(List.of(inventory));

		List<InventoryResponse> result = inventoryService.isInStock(List.of("iphone-12"));

		assertEquals(1, result.size());
		assertFalse(result.get(0).getIsInStock());
	}

	@Test
	void shouldReturnNotInStockForMissingSku() {
		when(inventoryRepository.findBySkuCodeIn(List.of("non-existent")))
				.thenReturn(List.of());

		List<InventoryResponse> result = inventoryService.isInStock(List.of("non-existent"));

		assertEquals(1, result.size());
		assertFalse(result.get(0).getIsInStock());
		assertEquals("non-existent", result.get(0).getSkuCode());
	}

	@Test
	void shouldHandleMixedStockStatus() {
		Inventory inStock = new Inventory();
		inStock.setSkuCode("iphone-15");
		inStock.setQuantity(50);

		Inventory outOfStock = new Inventory();
		outOfStock.setSkuCode("iphone-12");
		outOfStock.setQuantity(0);

		when(inventoryRepository.findBySkuCodeIn(List.of("iphone-15", "iphone-12", "missing")))
				.thenReturn(List.of(inStock, outOfStock));

		List<InventoryResponse> result = inventoryService.isInStock(List.of("iphone-15", "iphone-12", "missing"));

		assertEquals(3, result.size());
		assertTrue(result.get(0).getIsInStock());
		assertFalse(result.get(1).getIsInStock());
		assertFalse(result.get(2).getIsInStock());
	}
}
