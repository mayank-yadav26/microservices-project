package com.mayanktech.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayanktech.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order,Long>{

}
