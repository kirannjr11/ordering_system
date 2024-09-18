package table.order.table.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long id;
    private MenuDTO menu;
    private Integer quantity;

    @Override
    public String toString() {
        return "CartItemDTO{" +
                "id=" + id +
                ", menu=" + menu +
                ", quantity=" + quantity +
                '}';
    }
}

