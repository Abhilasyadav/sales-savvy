package com.salesSavvy.servies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesSavvy.entities.Cart;
import com.salesSavvy.entities.CartItem;
import com.salesSavvy.repositories.CartRepository;

@Service
public class CartServiceImplementation implements CartService {
	
	@Autowired
	CartRepository repo;

	public void addCart(Cart cart) {
		repo.save(cart);
	}

	

}
