/**
 * 
 * I declare that this code was written by me, 22017107. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Avril Leong
 * Student ID: 22017107
 * Class: E63C
 * Date created: 2024-Feb-06 8:13:50 am 
 * 
 */

package e63c.avril.ga;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author 22017107
 *
 */
@Controller
public class SearchController {
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model, RedirectAttributes redirectAttributes) {
        List<Item> items = itemRepository.searchByKeyword("%" + query + "%");
        if (items.size() == 1) {
            // If only one item is found, redirect to the item page
            return "redirect:/items/" + items.get(0).getId();
        } else {
            // If no items are found, redirect to a "no results" page or show a message
            model.addAttribute("message", "No items found");
            return "no_results"; // return the name of your "no results" view page
        }
    }
}