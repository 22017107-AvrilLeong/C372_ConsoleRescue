/**
 * 
 * I declare that this code was written by me, 22017107. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Avril Leong
 * Student ID: 22017107
 * Class: E63C
 * Date created: 2023-Nov-07 2:08:56 pm 
 * 
 */

package e63c.avril.ga;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@Controller
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping("/items")
	public String viewItems(Model model) {
		List<Item> listItems = itemRepository.findAll();

		model.addAttribute("listItems", listItems);
		return "view_items";
	}

	@GetMapping("/shop")
	public String shop(Model model) {
		List<Item> listItems = itemRepository.findAll();

		model.addAttribute("listItems", listItems);
		return "shop";
	}

	@GetMapping("items/add")
	public String addItem(Model model) {
		model.addAttribute("item", new Item());

		List<Category> catList = categoryRepository.findAll();
		model.addAttribute("catList", catList);
		return "add_item";
	}

	@GetMapping("/items/{id}")
	public String viewSingleItem(@PathVariable("id") Integer id, Model model) {
		Item item = itemRepository.getReferenceById(id);
		model.addAttribute("item", item);

		return "view_single_item";
	}

	@PostMapping("/items/save")
	public String saveItem(@Valid Item item, BindingResult bindingResult,
			@RequestParam("itemImage") MultipartFile imgFile, Model model) {
		if (bindingResult.hasErrors()) {
			List<Category> catList = categoryRepository.findAll();
			model.addAttribute("catList", catList);
			return "add_item";
		}

		String imageName = imgFile.getOriginalFilename();

		// Set the image name in item object
		item.setImgName(imageName);

		// save the item obj to the db
		Item savedItem = itemRepository.save(item);

		try {
			// Preparing the directory path
			String uploadDir = "uploads/items/" + savedItem.getId();
			Path uploadPath = Paths.get(uploadDir);
			System.out.println("Directory path: " + uploadPath);

			// checking if the upload path exists, if not it will be created
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			// Prepare path for file
			Path fileToCreatePath = uploadPath.resolve(imageName);
			System.out.println("File path: " + fileToCreatePath);

			// copy file to the upload location
			Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException io) {
			io.printStackTrace();
		}
		return "redirect:/items";
	}

	@GetMapping("/items/edit/{id}")
	public String editItem(@PathVariable("id") Integer id, Model model) {
		List<Category> catList = categoryRepository.findAll();
		model.addAttribute("catList", catList);

		Item item = itemRepository.getReferenceById(id);
		model.addAttribute("item", item);

		return "edit_item";
	}

	@PostMapping("/items/edit/{id}")
	public String saveUpdatedItem(@PathVariable("id") Integer id, @Valid Item item, BindingResult bindingResult,
			@RequestParam("itemImage") MultipartFile imgFile) {

		if (bindingResult.hasErrors()) {
			return "edit_item";
		}
		
		if (!imgFile.isEmpty()) {
			String imageName = imgFile.getOriginalFilename();
			System.out.println("Image name from imgFile: " + imageName);

			// Set the image name in item object
			item.setImgName(imageName);

			Item savedItem = itemRepository.save(item);

			try {
				// Preparing the directory path
				String uploadDir = "uploads/items/" + savedItem.getId();
				Path uploadPath = Paths.get(uploadDir);
				System.out.println("Directory path: " + uploadPath);

				// Checking if the upload path exists, if not it will be created.
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				// Prepare path for file
				Path fileToCreatePath = uploadPath.resolve(imageName);
				System.out.println("File path: " + fileToCreatePath);

				// Copy file to the upload location
				Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);

			} catch (IOException io) {
				io.printStackTrace();
			}
		} else // No edit to image, save the item object as passed.
		{
			System.out.println("Image name from item object: " + item.getImgName());
			itemRepository.save(item);
		}

		return "redirect:/items";
	}

	@GetMapping("/items/delete/{id}")
	public String deleteItem(@PathVariable("id") Integer id) {
		itemRepository.deleteById(id);

		return "redirect:/items";
	}
}
