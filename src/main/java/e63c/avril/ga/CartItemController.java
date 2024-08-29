package e63c.avril.ga;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartItemController {

	@Autowired
	private CartItemRepository cartItemRepo;

	@Autowired
	private ItemRepository itemRepo;

	@Autowired
	private MemberRepository memberRepo;

	@Autowired
	private OrderItemRepository orderRepo;

	@Autowired
	private JavaMailSender javaMailSender;

	@GetMapping("/cart")
	public String showCart(Model model, Principal principal) {

		/**
		 * Complete the code for the showCart method. The comments below are meant as a
		 * guide. Step 1 has been completed for you.
		 **/
		// Step 1: Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();

		// Step 2: Get shopping cart items added by this user
		// *Hint: You will need to use the method we added in the CartItemRepository

		List<CartItem> cartItemList = cartItemRepo.findAllByMemberId(loggedInMemberId);

		// Step 3: Add the shopping cart items to the model

		model.addAttribute("cartItemList", cartItemList);

		// Step 4: Calculate the total cost of all items in the shopping cart

		double cartTotal = 0.0;

		for (int i = 0; i < cartItemList.size(); i++) {
			CartItem currentCartItem = cartItemList.get(i);
			int itemQuantityInCart = currentCartItem.getQuantity();

			Item item = currentCartItem.getItem();
			double itemPrice = item.getPrice();

			cartTotal += itemPrice * itemQuantityInCart;

		}
		// Step 5: Add the shopping cart total to the model
		model.addAttribute("cartTotal", String.format("%.2f", cartTotal));
		model.addAttribute("memberId", loggedInMemberId);

		return "cart";
	}

	@PostMapping("/cart/add/{itemId}")
	public String addToCart(@PathVariable("itemId") int itemId, @RequestParam("quantity") int quantity,
			Principal principal, Model model) {

		/**
		 * Complete the code for the addCart method. The comments below are meant as a
		 * guide.
		 **/

		// Step 1: Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();

		// Step 2: Check in the cartItemRepo if item was previously added by user.
		// *Hint: we will need to write a new method in the CartItemRepository
		CartItem cartItem = cartItemRepo.findByMemberIdAndItemId(loggedInMemberId, itemId);

		// Step 3: if the item was previously added, then we get the quantity that was
		// previously added and increase that
		// Save the CartItem object back to the repository
		if (cartItem != null) {
			int currQty = cartItem.getQuantity();
			int availableQuantity = cartItem.getItem().getQuantity();

			if (quantity + currQty > availableQuantity) {
				quantity = availableQuantity - currQty;
				model.addAttribute("quantityError", "Quantity cannot exceed available quantity.");
			}
			
		    // Check if quantity is greater than 0
		    if (quantity <= 0) {
		        model.addAttribute("quantityError", "Quantity must be greater than 0.");
		        return "redirect:/cart";
		    }

			cartItem.setQuantity(quantity + currQty);
			cartItemRepo.save(cartItem);
		} else {

			// Step 4: if the item was NOT previously added,
			// then prepare the item and member objects

			Item item = itemRepo.getReferenceById(itemId);
			Member member = memberRepo.getReferenceById(loggedInMemberId);

			// Step 5: Create a new CartItem object
			CartItem newCartItem = new CartItem();

			// Step 6: Set the item and member as well as the new quantity in the new
			// CartItem
			// object
			newCartItem.setItem(item);
			newCartItem.setMember(member);

			if (quantity > item.getQuantity()) {
				quantity = item.getQuantity();
				model.addAttribute("quantityError", "Quantity cannot exceed available quantity.");
			}
			
		    // Check if quantity is greater than 0
		    if (quantity <= 0) {
		        model.addAttribute("quantityError", "Quantity must be greater than 0.");
		        return "redirect:/cart";
		    }

			newCartItem.setQuantity(quantity);
			// Step 7: Save the new CartItem object to the repository
			cartItemRepo.save(newCartItem);

		}

		return "redirect:/cart";
	}

	@PostMapping("/cart/update/{id}")
	public String updateCart(@PathVariable("id") int cartItemId, @RequestParam("qty") int qty, Model model) {

		// Step 1: Get cartItem object by cartItem's id
		CartItem cartItem = cartItemRepo.getReferenceById(cartItemId);

		// Step 2: Set the quantity of the carItem object retrieved
		int availableQuantity = cartItem.getItem().getQuantity();
		
		if (qty > availableQuantity) {
			// If it exceeds, set the quantity to the available quantity
			qty = availableQuantity;

			model.addAttribute("quantityError", "Quantity cannot exceed available quantity.");
		} else {
			model.addAttribute("quantityError", null);
		}
		
	    // Check if quantity is greater than 0
	    if (qty <= 0) {
	        model.addAttribute("quantityError", "Quantity must be greater than 0.");
	        return "redirect:/cart";
	    }

		cartItem.setQuantity(qty);
		// Step 3: Save the cartItem back to the cartItemRepo
		cartItemRepo.save(cartItem);

		return "redirect:/cart";
	}

	@GetMapping("/cart/remove/{id}")
	public String removeFromCart(@PathVariable("id") int cartItemId) {

		// Step 1: Remove item from cartItemRepo
		cartItemRepo.deleteById(cartItemId);

		return "redirect:/cart";
	}

	@PostMapping("/cart/process_order")
	public String processOrder(Model model, @RequestParam("cartTotal") double cartTotal,
			@RequestParam("memberId") int memberId, @RequestParam("orderId") String orderId,
			@RequestParam("transactionId") String transactionId) {

		// 1. Retrieve the member object
		Member member = memberRepo.getReferenceById(memberId);

		// 2. Retrieve all cart items, paid for by the member
		List<CartItem> cartItemList = cartItemRepo.findAllByMemberId(memberId);

		// Create a list to hold OrderItems
//		List<OrderItem> orderItemList = new ArrayList<>();

		// 3. For each cart item paid,
		for (int i = 0; i < cartItemList.size(); i++) {

			// a) Retrieve the item id, item, and quantity of the item purchased
			CartItem currentCartItem = cartItemList.get(i);
			Item itemToUpdate = currentCartItem.getItem();
			int quantityOfItemPurchased = currentCartItem.getQuantity();
			int itemToUpdateId = itemToUpdate.getId();

			System.out.println("Item: " + itemToUpdate.getDescription());
			// b) Update the item’s quantity in the item repository
			Item inventoryItem = itemRepo.getReferenceById(itemToUpdateId);
			int currentInventoryQuantity = inventoryItem.getQuantity();
			System.out.println("Current Inventory Quantity: " + inventoryItem.getQuantity());
			inventoryItem.setQuantity(currentInventoryQuantity - quantityOfItemPurchased);
			itemRepo.save(inventoryItem);

			// c) Add the member, item, quantity of item purchased, order id, and
			// transaction id
			// into the order repository
			OrderItem orderItem = new OrderItem();
			orderItem.setMember(member);
			orderItem.setItem(itemToUpdate);
			orderItem.setQuantity(quantityOfItemPurchased);
			orderItem.setOrderId(orderId);
			orderItem.setTransactionId(transactionId);

			orderRepo.save(orderItem);

			// Set the subTotal based on the quantity and item price

			// d) Clear the items purchased by the member from the cart item repository
			cartItemRepo.deleteById(currentCartItem.getId());
		}

		// 4. Add the cart total, cart item list, member object, order id, and
		// transaction id to the model
		// for display on the acknowledgement page
		model.addAttribute("cartTotal", cartTotal);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("member", member);
		model.addAttribute("orderId", orderId);
		model.addAttribute("transactionId", transactionId);

		// 5. Send email - To be completed.
		
		String subject = "ConsoleRescue Order Confirmation";
		StringBuilder body = new StringBuilder();
		body.append("Dear ").append(member.getName()).append(",\n\n");
		body.append("Thank you for shopping with ConsoleRescue! Here are the details of your order:\n\n");
		body.append("Order ID: ").append(orderId).append("\n");
		body.append("Transaction ID: ").append(transactionId).append("\n");
		body.append("Total: ").append(cartTotal).append("\n\n");
		body.append("Items:\n");

		for (CartItem cartItem : cartItemList) {
		    Item item = cartItem.getItem();
		    double subtotal = item.getPrice() * cartItem.getQuantity(); // 

		    body.append("- ").append(item.getDescription())
		        .append(" (Quantity: ").append(cartItem.getQuantity())
		        .append(", Subtotal: ").append(subtotal)
		        .append(")\n");
		}

		body.append("\nWe appreciate your patrionage and look forward to serving you again soon!\n\n");
		body.append("Best regards,\n");
		body.append("ConsoleRescue Team");

		String to = "22017107@myrp.edu.sg"; 

		sendEmail(to, subject, body.toString());

		
		return "success";
	}

	public void sendEmail(String to, String subject, String body) {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(body);
//		msg.setFrom("pokedoemc@gmail.com");v

		System.out.println("Sending");
		javaMailSender.send(msg);
		System.out.println("Sent");
	}

}
