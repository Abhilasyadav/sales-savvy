package com.salesSavvy.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.salesSavvy.DTO.OrderDTO;
import com.salesSavvy.entities.Orders;
import com.salesSavvy.entities.Users;
import com.salesSavvy.repositories.OrderRepository;
import com.salesSavvy.repositories.UsersRepository;

@RestController
@CrossOrigin("*")
@RequestMapping("/orders")
public class OrderController {



    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private UsersRepository userRepo;

    
    

    @GetMapping("/getorders/{username}")
    public ResponseEntity<?> getFilteredOrdersByUser(@PathVariable String username) {
    	username = username.trim();
        Users user = userRepo.findByUsername(username);
        if (user == null) return ResponseEntity.badRequest().body("User not found");

        List<Orders> orders = orderRepo.findByUser(user).stream()
                .filter(order -> !"CREATED".equalsIgnoreCase(order.getStatus()))
                .sorted((a, b) -> b.getOrderTime().compareTo(a.getOrderTime()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(orders);
    }

    @PutMapping("/request-return/{orderId}")
    public ResponseEntity<String> requestReturn(@PathVariable String orderId) {
        Orders order = orderRepo.findById(orderId).orElse(null);
        if (order == null) return ResponseEntity.badRequest().body("Order not found");

        if (!"DELIVERED".equalsIgnoreCase(order.getStatus())) {
            return ResponseEntity.status(400).body("Order not delivered yet");
        }

        order.setStatus("RETURN_REQUESTED");
        orderRepo.save(order);

        return ResponseEntity.ok("Return requested successfully");
    }


    @GetMapping("/admin/all")
    public ResponseEntity<List<OrderDTO>> getAllOrdersForAdmin() {
        List<Orders> orders = orderRepo.findAll().stream()
                .filter(order -> !"CREATED".equalsIgnoreCase(order.getStatus()))
                .sorted((a, b) -> b.getOrderTime().compareTo(a.getOrderTime()))
                .collect(Collectors.toList());

        List<OrderDTO> dtos = orders.stream().map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setAmount(order.getAmount());
            dto.setStatus(order.getStatus());
            dto.setPaymentId(order.getPaymentId());
            dto.setUsername(order.getUser().getUsername()); 
            dto.setOrderTime(order.getOrderTime());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }


    @PutMapping("/admin/orders/{orderId}/status")
    public String updateOrderStatus(@PathVariable String orderId, @RequestBody String newStatus) {
        Orders order = orderRepo.findById(orderId).orElse(null);
        if (order == null) return "Order not found";

        order.setStatus(newStatus.replace("\"", "")); 

        if ("DELIVERED".equalsIgnoreCase(order.getStatus())) {
            order.setDeliveryTime(LocalDateTime.now());
        }

        orderRepo.save(order);
        return "Order status updated";
    }
}
