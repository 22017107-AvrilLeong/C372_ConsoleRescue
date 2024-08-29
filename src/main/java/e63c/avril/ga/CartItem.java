/**
 * 
 * I declare that this code was written by me, 22017107. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Avril Leong
 * Student ID: 22017107
 * Class: E63C
 * Date created: 2024-Jan-23 9:58:08 am 
 * 
 */

package e63c.avril.ga;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

/**
 * @author 22017107
 *
 */
@Entity
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;
	private int quantity;

	@Transient
	private double subTotal;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getSubTotal() {
		subTotal = quantity * item.getPrice();
		return subTotal;

	}
	
	public String getFormattedSubTotal() {
	    double subTotal = quantity * item.getPrice();
	    return String.format("%.2f", subTotal);
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

}
