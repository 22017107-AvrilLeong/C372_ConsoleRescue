/**
 * 
 * I declare that this code was written by me, 22017107. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Avril Leong
 * Student ID: 22017107
 * Class: E63C
 * Date created: 2024-Jan-09 3:39:15 pm 
 * 
 */

package e63c.avril.ga;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 22017107
 *
 */
@Controller
public class LoginController {
	@GetMapping("/login")
	public String login() {
		return "login";
	}
}