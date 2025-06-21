package com.salesSavvy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesSavvy.entities.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

}
