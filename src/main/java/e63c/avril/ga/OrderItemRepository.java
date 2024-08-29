package e63c.avril.ga;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	public List<OrderItem> findAllByMemberId(int id);
	
}
