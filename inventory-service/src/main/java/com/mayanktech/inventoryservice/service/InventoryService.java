package com.mayanktech.inventoryservice.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mayanktech.common.dto.InventoryAdjustmentRequest;
import com.mayanktech.common.dto.InventoryResponse;
import com.mayanktech.inventoryservice.dto.InventoryRequest;
import com.mayanktech.inventoryservice.model.Inventory;
import com.mayanktech.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

	private final InventoryRepository inventoryRepository;

	@Transactional(readOnly = true)
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
							.quantity(inventory != null ? inventory.getQuantity() : 0)
							.build();
				}).toList();
	}

	@Transactional
	public InventoryResponse upsert(InventoryRequest inventoryRequest) {
		Inventory inventory = inventoryRepository.findBySkuCode(inventoryRequest.getSkuCode())
				.orElseGet(Inventory::new);

		inventory.setSkuCode(inventoryRequest.getSkuCode());
		inventory.setQuantity(inventoryRequest.getQuantity());

		Inventory saved = inventoryRepository.save(inventory);
		return InventoryResponse.builder()
				.skuCode(saved.getSkuCode())
				.isInStock(saved.getQuantity() > 0)
				.quantity(saved.getQuantity())
				.build();
	}

	@Transactional
	public List<InventoryResponse> decrease(List<InventoryAdjustmentRequest> adjustments) {
		return adjustments.stream().map(adjustment -> {
			int updated = inventoryRepository.decrementStock(adjustment.getSkuCode(), adjustment.getQuantity());

			Inventory inventory = inventoryRepository.findBySkuCode(adjustment.getSkuCode())
					.orElseGet(() -> {
						Inventory missing = new Inventory();
						missing.setSkuCode(adjustment.getSkuCode());
						missing.setQuantity(0);
						return missing;
					});

			boolean inStock = updated > 0 && inventory.getQuantity() > 0;
			return InventoryResponse.builder()
					.skuCode(adjustment.getSkuCode())
					.isInStock(inStock)
					.quantity(inventory.getQuantity())
					.build();
		}).toList();
	}
}
