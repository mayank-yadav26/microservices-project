package com.mayanktech.inventoryservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayanktech.inventoryservice.modal.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory,Long>{

	List<Inventory> findBySkuCodeIn(List<String> skuCodeList);

}
