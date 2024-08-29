/**
 * 
 * I declare that this code was written by me, 22017107. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Avril Leong
 * Student ID: 22017107
 * Class: E63C
 * Date created: 2023-Nov-07 12:12:20 pm 
 * 
 */

package e63c.avril.ga;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * @author 22017107
 *
 */

@Entity
public class Category {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	
	private int id;
	
	@NotEmpty(message= "Category name cannot be empty!")
	@Size(min =3, max= 10, message= "Category length must be between 3 and 10 characters!")
	private String name;
	
	@OneToMany(mappedBy = "category")
	private Set<Item> items;
	
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

}
