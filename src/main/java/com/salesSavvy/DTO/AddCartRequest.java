package com.salesSavvy.DTO;

public class AddCartRequest {
	
	private String username;
    private Long productId;
    private int quantity;
	public AddCartRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AddCartRequest(String username, Long productId, int quantity) {
		super();
		this.username = username;
		this.productId = productId;
		this.quantity = quantity;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "AddCartRequest [username=" + username + ", productId=" + productId + ", quantity=" + quantity + "]";
	}
    
    

}

