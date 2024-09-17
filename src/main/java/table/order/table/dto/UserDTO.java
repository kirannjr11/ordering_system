package table.order.table.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import table.order.table.Enum.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String phoneNumber;
    private String password;
    private Role role;
}
