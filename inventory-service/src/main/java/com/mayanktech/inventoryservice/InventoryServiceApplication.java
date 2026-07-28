package com.mayanktech.inventoryservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.mayanktech.inventoryservice.model.Inventory;
import com.mayanktech.inventoryservice.repository.InventoryRepository;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			if (inventoryRepository.count() == 0) {
				Inventory inventory = new Inventory();
				inventory.setSkuCode("iphone 13");
				inventory.setQuantity(100);

				Inventory inventory1 = new Inventory();
				inventory1.setSkuCode("iphone 12");
				inventory1.setQuantity(0);

				inventoryRepository.save(inventory);
				inventoryRepository.save(inventory1);
			}
		};
	}

}
