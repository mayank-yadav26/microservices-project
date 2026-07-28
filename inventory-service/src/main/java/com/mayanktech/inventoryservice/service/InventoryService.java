package com.mayanktech.inventoryservice.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mayanktech.common.dto.InventoryResponse;
import com.mayanktech.inventoryservice.model.Inventory;
import com.mayanktech.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
	
	private final InventoryRepository inventoryRepository;
	
	@Transactional(readOnly=true)
	public List<InventoryResponse> isInStock(List<String> skuCodeList) {
		Map<String, Inventory> inventoryMap = inventoryRepository.findBySkuCodeIn(skuCodeList).stream()
				.collect(Collectors.toMap(Inventory::getSkuCode, inventory -> inventory));

		return skuCodeList.stream()
				.map(skuCode -> {
					Inventory inventory = inventoryMap.get(skuCode);
					boolean inStock = inventory != null && inventory.getQuantity() > 0;
					return InventoryResponse.builder()
							.skuCode(skuCode)
							.isInStock(inStock)
							.build();
				}).toList();
	}
}
