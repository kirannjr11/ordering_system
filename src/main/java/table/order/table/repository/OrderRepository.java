package table.order.table.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import table.order.table.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByTableId(Long tableId);
}
