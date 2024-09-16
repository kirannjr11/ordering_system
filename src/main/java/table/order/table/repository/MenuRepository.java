package table.order.table.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import table.order.table.model.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
