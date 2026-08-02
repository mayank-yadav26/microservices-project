package com.mayanktech.inventoryservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mayanktech.inventoryservice.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory,Long>{

	List<Inventory> findBySkuCodeIn(List<String> skuCodeList);

	Optional<Inventory> findBySkuCode(String skuCode);

	@Modifying
	@Query("UPDATE Inventory i SET i.quantity = i.quantity - :quantity WHERE i.skuCode = :skuCode AND i.quantity >= :quantity")
	int decrementStock(@Param("skuCode") String skuCode, @Param("quantity") Integer quantity);

}
