/**
 * 
 * I declare that this code was written by me, 22017107. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Avril Leong
 * Student ID: 22017107
 * Class: E63C
 * Date created: 2024-Jan-02 10:13:54 am 
 * 
 */

package e63c.avril.ga;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 22017107
 *
 */
public interface MemberRepository extends JpaRepository<Member, Integer>{
	public Member findByUsername(String username);
}
