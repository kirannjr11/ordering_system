package table.order.table.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import table.order.table.dto.UserDTO;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO existingUserDTO = userService.getUserById(id);
        if (existingUserDTO == null) {
            throw new InvalidUserDataException("User not found with ID: " + id);
        }
        return userService.updateUser(id, userDTO);
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO == null) {
            throw new InvalidUserDataException("User not found with ID: " + id);
        }
        return userDTO;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO == null) {
            throw new InvalidUserDataException("User not found with ID: " + id);
        }
        userService.deleteUser(id);
    }
}
