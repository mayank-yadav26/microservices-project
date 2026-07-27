package com.mayanktech.inventoryservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mayanktech.common.dto.InventoryResponse;
import com.mayanktech.inventoryservice.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
	
	private final InventoryService inventoryService;

//	@GetMapping("/{sku-code}")
//	@ResponseStatus(HttpStatus.OK)
//	public boolean isInStock(@PathVariable("sku-code") String skuCode) {
//		return inventoryService.isInStock(skuCode);
//	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<InventoryResponse> isInStock(@RequestParam List<String> skuCodeList) {
		return inventoryService.isInStock(skuCodeList);
	}
}
