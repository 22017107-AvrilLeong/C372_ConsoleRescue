/**
 * 
 * I declare that this code was written by me, 22017107. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Avril Leong
 * Student ID: 22017107
 * Class: E63C
 * Date created: 2024-Jan-02 10:09:32 am 
 * 
 */

package e63c.avril.ga;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author 22017107
 *
 */
@Controller
public class MemberController {

	@Autowired
	private MemberRepository memberRepository;

	@GetMapping("/members")
	public String viewMembers(Model model) {
		List<Member> listMembers = memberRepository.findAll();

		model.addAttribute("listMembers", listMembers);
		return "view_members";
	}

	@GetMapping("/account")
	public String viewAccount(Model model) {
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();

		Member member = memberRepository.getReferenceById(loggedInMemberId);

		model.addAttribute("member", member);

		return "account";
	}

	@GetMapping("/members/add")
	public String addMember(Model model) {
		model.addAttribute("member", new Member());
		return "add_member";
	}

	@PostMapping("/members/save")
	public String saveMember(Member member, RedirectAttributes redirectAttribute) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(member.getPassword());

		member.setPassword(encodedPassword);
		member.setRole("ROLE_USER");

		memberRepository.save(member);

		redirectAttribute.addFlashAttribute("success", "Member Registered!");
		return "redirect:/members";
	}
	
	@GetMapping("/register")
	public String registerAcc(Model model) {
		model.addAttribute("member", new Member());
		
		return "register";
	}

	@PostMapping("/register/save")
	public String saveAcc(Member member, RedirectAttributes redirectAttribute) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(member.getPassword());

		member.setPassword(encodedPassword);
		member.setRole("ROLE_USER");

		memberRepository.save(member);

		redirectAttribute.addFlashAttribute("success", "Account Registered!");
		return "redirect:/";
	}

	@GetMapping("/members/edit/{id}")
	public String editMember(@PathVariable("id") Integer id, Model model) {

		Member member = memberRepository.getReferenceById(id);
		model.addAttribute("member", member);

		return "edit_member";
	}

	@PostMapping("/members/edit/{id}")
	public String saveUpdatedMember(@PathVariable("id") Integer id, Member member) {

		memberRepository.save(member);
		return "redirect:/members";
	}

	@GetMapping("/members/delete/{id}")
	public String deleteMember(@PathVariable("id") Integer id) {
		memberRepository.deleteById(id);

		return "redirect:/members";
	}

}
