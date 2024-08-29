/**
 * 
 * I declare that this code was written by me, 22017107. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Avril Leong
 * Student ID: 22017107
 * Class: E63C
 * Date created: 2023-Nov-07 2:06:39 pm 
 * 
 */

package e63c.avril.ga;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Integer>{
	
    @Query("SELECT i FROM Item i WHERE i.name LIKE :keyword OR i.description LIKE :keyword")
    List<Item> searchByKeyword(@Param("keyword") String keyword);    
}

