package table.order.table.repositorroes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import table.order.table.models.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
