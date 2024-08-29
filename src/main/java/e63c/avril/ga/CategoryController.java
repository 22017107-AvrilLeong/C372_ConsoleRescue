/**
 * 
 * I declare that this code was written by me, 22017107. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Avril Leong
 * Student ID: 22017107
 * Class: E63C
 * Date created: 2023-Nov-07 12:49:51 pm 
 * 
 */

package e63c.avril.ga;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class CategoryController {

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping("/categories")
	public String viewCategories(Model model) {
		List<Category> listCategories = categoryRepository.findAll();

		model.addAttribute("listCategories", listCategories);
		return "view_categories";
	}

	@GetMapping("/categories/add")
	public String addCategory(Model model) {
		model.addAttribute("category", new Category());
		return "add_category";
	}

	@PostMapping("/categories/save")
	public String saveCategory(@Valid Category category, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
		{
			return "add_category";
		}
		categoryRepository.save(category);
		return "redirect:/categories";
	}

	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable("id") Integer id, Model model) {

		Category category = categoryRepository.getReferenceById(id);
		model.addAttribute("category", category);

		return "edit_category";
	}

	@PostMapping("/categories/edit/{id}")
	public String saveUpdatedCategory(@PathVariable("id") Integer id, Category category) {

		categoryRepository.save(category);
		return "redirect:/categories";
	}

	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable("id") Integer id) {
		categoryRepository.deleteById(id);

		return "redirect:/categories";
	}

}
