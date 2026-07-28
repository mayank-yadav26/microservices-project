package com.mayanktech.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mayanktech.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{

}
