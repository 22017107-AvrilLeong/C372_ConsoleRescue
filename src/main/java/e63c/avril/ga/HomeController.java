/**
 * 
s * I declare that this code was written by me, 22017107. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Avril Leong
 * Student ID: 22017107
 * Class: E63C
 * Date created: 2023-Nov-14 12:21:45 pm 
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
public class HomeController {
	@GetMapping("/")
	public String home() {
		return("index");
	}
	
	@GetMapping("/about")
	public String about() {
		return("about");
	}
	
	@GetMapping("/contact")
	public String contact() {
		return("contact");
	}
	

	@GetMapping("/403")
	public String error403() {
		return "403";
	}
	
}
