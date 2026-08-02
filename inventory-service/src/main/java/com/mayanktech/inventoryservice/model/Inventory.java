package com.mayanktech.inventoryservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="t_inventory", uniqueConstraints = @UniqueConstraint(columnNames = "sku_code"))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String skuCode;
	private Integer quantity;
}
