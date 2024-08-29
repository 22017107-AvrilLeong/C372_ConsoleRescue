/**
 * 
 * I declare that this code was written by me, 22017107. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Avril Leong
 * Student ID: 22017107
 * Class: E63C
 * Date created: 2024-Feb-03 10:25:01 pm 
 * 
 */

package e63c.avril.ga;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 22017107
 *
 */

@Controller
public class OrderItemController {
	
	@Autowired
	private CartItemRepository cartItemRepo;

	@Autowired
	private ItemRepository itemRepo;

	@Autowired
	private MemberRepository memberRepo;

	@Autowired
	private OrderItemRepository orderRepo;
	
	@GetMapping("/transaction")
	public String showHistory(Model model, Principal principal) {

		// Step 1: Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();

		// Step 2: Get shopping cart items added by this user
		// *Hint: You will need to use the method we added in the CartItemRepository

		List<OrderItem> orderItemList = orderRepo.findAllByMemberId(loggedInMemberId);

		model.addAttribute("orderItemList", orderItemList);

		return "transaction_history";
	}
	
	@GetMapping("/member/transaction/{id}")
	public String showMemberHist(@PathVariable("id") int memberId, Model model, Principal principal) {

		// Step 1: Get currently logged in user
		Member member = memberRepo.getReferenceById(memberId);

		// Step 2: Get shopping cart items added by this user
		// *Hint: You will need to use the method we added in the CartItemRepository

		List<OrderItem> orderItemList = orderRepo.findAllByMemberId(memberId);

		model.addAttribute("orderItemList", orderItemList);

		return "transaction_history";
	}
}
