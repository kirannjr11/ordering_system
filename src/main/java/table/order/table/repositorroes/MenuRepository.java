package table.order.table.repositorroes;

import org.springframework.data.jpa.repository.JpaRepository;
import table.order.table.models.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
