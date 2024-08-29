/**
 * 
 * I declare that this code was written by me, 22017107. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Avril Leong
 * Student ID: 22017107
 * Class: E63C
 * Date created: 2023-Nov-07 2:04:49 pm 
 * 
 */

package e63c.avril.ga;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
public class Item{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	
	private int id;
	
	@NotEmpty(message= "Item name cannot be empty!")
	@Size(min =5, max= 100, message= "Item name length must be more than or equals to 5 characters!")
	private String name;
	
	@NotEmpty(message= "Item description cannot be empty!")
	@Size(min =5, max= 100, message= "Item description length must be more than or equals to 5 characters!")
	private String description;
	
	@DecimalMin(value = "0.0", message = "Item price must be a positive number!")
	private double price;
	
	@Min(value = 0, message = "Item quantity must be a positive number!")
	private int quantity;
	private String imgName;
	
	@ManyToOne
	@JoinColumn(name ="category_id", nullable=false)
	private Category category;
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
	
}
