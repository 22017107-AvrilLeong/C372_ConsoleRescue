/**
 * 
 * I declare that this code was written by me, 22017107. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Avril Leong
 * Student ID: 22017107
 * Class: E63C
 * Date created: 2024-Jan-02 10:08:06 am 
 * 
 */

package e63c.avril.ga;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * @author 22017107
 *
 */

@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotEmpty(message = "Name is required")
	@Size(min = 5, max = 50, message = "Should have minimum length of 5, maximum length of 50")
	private String name;

	@NotEmpty(message = "Username is required")
	@Size(min = 5, max = 20, message = "Should have minimum length of 5, maximum length of 20")
	private String username;

	@NotEmpty(message = "Password is required")
	@Size(min = 5, max = 200, message = "Should have minimum length of 5, maximum length of 200")
	private String password;

	@NotEmpty(message = "Email is required")
	@Size(min = 5, max = 50, message = "Should have minimum length of 5, maximum length of 50")
	private String email;
	private String role;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
