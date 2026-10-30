package com.vube.buildMySetup.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vube.buildMySetup.model.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order ,Long> {
	
}
