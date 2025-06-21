package com.salesSavvy.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salesSavvy.DTO.AddCartRequest;
import com.salesSavvy.entities.Cart;

import com.salesSavvy.entities.CartItem;
import com.salesSavvy.entities.Product;
import com.salesSavvy.entities.Users;
import com.salesSavvy.servies.CartService;
import com.salesSavvy.servies.ProductService;
import com.salesSavvy.servies.UsersService;


@CrossOrigin("*")
@RestController
public class ProductController {

	
	@Autowired
	ProductService service;
	
	@Autowired
	UsersService uService;
	
	@Autowired
	CartService cService;
	
	@PostMapping("/addProduct")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> addProduct(@RequestBody Product product) {
        String result = service.addProduct(product);

        Map<String, String> response = new HashMap<>();
        response.put("message", result);

        return ResponseEntity.ok(response);  
    }
	
	@PostMapping("/updateProduct")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, String>> updateProduct(@RequestBody Product product) {
		String result =  service.updateProduct(product);
		
		Map<String, String> response = new HashMap<>();
		response.put("message", result);
		
		return ResponseEntity.ok(response);
	}
//	
//	@GetMapping("/seachProductById")
//	public Product seachById(@RequestParam Long id) {
//		return service.seachProduct(id);
//	}
//	
//	@GetMapping("/seachProductByName")
//	public Product seachByName(@RequestParam String name) {
//		return service.seachProduct(name);
//	}
//	
	@GetMapping("/seachProduct")
	@PreAuthorize("hasRole('USER')")
	public List<Product> seachByCategory(@RequestParam(required = false) String name, @RequestParam(required = false) String category) {
		return service.searchProducts(name, category);
	}
	
	@DeleteMapping("/deleteProduct/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String deletePRoduct(@PathVariable Long id) {
		System.out.println(id);
		return service.deleteProduct(id);
	}
	
	@GetMapping("/getAllProduct")
	@PreAuthorize("hasRole('USER')")
	public List<Product> getAllProduct(){
		return service.getAllProducts();
	}
	
	@GetMapping("/getProductById/{id}")
	@PreAuthorize("hasRole('USER')")
    public Product getProductById(@PathVariable Long id) {
        return service.getProductById(id);
    }
	
	
	
	@PostMapping("/addToCart")
	@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addToCart(@RequestBody AddCartRequest request) {
		System.out.println(request);
        if (request == null || request.getProductId() == 0) {
            return ResponseEntity.badRequest().body("Product cannot be null.");
        }

        Users user = uService.getUser(request.getUsername());
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        Long productId = request.getProductId();

        Product product = service.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
            cService.addCart(cart); 
        }

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null) {
        	cartItems = new ArrayList<>();
        }
        

        boolean found = false;
        for (CartItem ci : cartItems) {
            if (ci.getProduct().getId() == product.getId()) {
                ci.setQuantity(ci.getQuantity() + request.getQuantity());
                found = true;
                break;
            }
        }

        if (!found) {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cartItems.add(newItem);
        }

        cart.setCartItems(cartItems);
        cService.addCart(cart); 

        return ResponseEntity.ok("Product added to cart successfully.");
    }

	@GetMapping("/getCart/{username}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getCart(@PathVariable String username) {
	    Users user = uService.getUser(username);
	    
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("User not found: " + username);
	    }

	    if (user.getCart() == null || user.getCart().getCartItems() == null) {
	        return ResponseEntity.ok(Collections.emptyList());
	    }

	    return ResponseEntity.ok(user.getCart().getCartItems());
	}

    
    @DeleteMapping("/deleteCart/{username}/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public List<CartItem> deleteCart(@PathVariable String username, long itemId) {
        Users user = uService.getUser(username);
        Product product = service.getProductById(itemId);
        if (user == null || user.getCart() == null) {
            return new ArrayList<>();
        }
        return user.getCart().getCartItems();
    }
    
	
    
    @PutMapping("/updateCart/{username}/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CartItem>> updateCart(
            @PathVariable String username,
            @PathVariable long itemId,
            @RequestParam("quantity") int change) {

        Users user = uService.getUser(username);
        Product product = service.getProductById(itemId);

        if (user == null || user.getCart() == null) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        return ResponseEntity.ok(user.getCart().getCartItems());
    }

	

	
}
