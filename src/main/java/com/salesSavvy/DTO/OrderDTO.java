package com.salesSavvy.DTO;

import java.time.LocalDateTime;

public class OrderDTO {
    private String id;
    private double amount;
    private String status;
    private String paymentId;
    private String username; // from user
    private LocalDateTime orderTime;
	public OrderDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrderDTO(String id, double amount, String status, String paymentId, String username,
			LocalDateTime orderTime) {
		super();
		this.id = id;
		this.amount = amount;
		this.status = status;
		this.paymentId = paymentId;
		this.username = username;
		this.orderTime = orderTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public LocalDateTime getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}
    
    
}
