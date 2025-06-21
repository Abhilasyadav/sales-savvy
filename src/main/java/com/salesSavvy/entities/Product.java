package com.salesSavvy.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Product {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;
    private double price;
    private String photo;
    private String category;

    @ElementCollection
    @CollectionTable(
        name = "product_reviews"
//        joinColumns = @JoinColumn(name = "product_id")
    )
    @Column(name = "review")
    private List<String> reviews = new ArrayList<>();
    
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "product-cartItems")
    private List<CartItem> cartItems = new ArrayList<>();


    public Product() {
        super();
    }

    

    public Product(long id, String name, String description, double price, String photo, String category,
		List<String> reviews) {
	super();
	this.id = id;
	this.name = name;
	this.description = description;
	this.price = price;
	this.photo = photo;
	this.category = category;
	this.reviews = reviews;
}
    


	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public double getPrice() {
		return price;
	}



	public void setPrice(double price) {
		this.price = price;
	}



	public String getPhoto() {
		return photo;
	}



	public void setPhoto(String photo) {
		this.photo = photo;
	}



	public String getCategory() {
		return category;
	}



	public void setCategory(String category) {
		this.category = category;
	}



	public List<String> getReviews() {
		return reviews;
	}



	public void setReviews(List<String> reviews) {
		this.reviews = reviews;
	}



	@Override
    public String toString() {
        return "Product{" +
               "name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", price=" + price +
               ", photo='" + photo + '\'' +
               ", category='" + category + '\'' +
               ", reviews=" + reviews +
               '}';
    }
}
