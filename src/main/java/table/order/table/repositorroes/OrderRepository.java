package table.order.table.repositorroes;

import org.springframework.data.jpa.repository.JpaRepository;
import table.order.table.models.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
