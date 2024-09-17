package table.order.table.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import table.order.table.dto.UserDTO;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.model.Role;
import table.order.table.model.User;
import table.order.table.repository.RoleRepository;
import table.order.table.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service

public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // create user
    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO.getPhoneNumber() == null || userDTO.getPhoneNumber().isEmpty()) {
            throw new InvalidUserDataException("phone number can not be null");
        }

        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new InvalidUserDataException("Password cannot be null or empty");
        }

        if (!userDTO.getRole().equalsIgnoreCase("USER") && !userDTO.getRole().equalsIgnoreCase("ADMIN")) {
            throw new InvalidUserDataException("Invalid role: Only 'USER' or 'ADMIN' are allowed.");
        }

        User user = new User();
        user.setPhoneNumber(userDTO.getPhoneNumber());

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encodedPassword);

        Role role = new Role();
        role.setRole(userDTO.getRole());
        user.setRole(role);

        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser.getId(), savedUser.getPhoneNumber(), savedUser.getPassword(), savedUser.getRole().getRole());
    }

    // get User by id
    public UserDTO getUserById(Long userId) {
        if (userId == null) {
            throw new InvalidUserDataException("User id can not be null");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidUserDataException("User not found with id :" + userId));
        return new UserDTO(user.getId(), user.getPhoneNumber(), user.getPassword(), user.getRole().getRole());
    }


    // get user by phone number
    public UserDTO getUserByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new InvalidUserDataException("Phone number cannot be null or empty");
        }

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new InvalidUserDataException("User not found with phone number: " + phoneNumber));

        return new UserDTO(user.getId(), user.getPhoneNumber(), user.getPassword(), user.getRole().getRole());
    }


    // list all users
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getPhoneNumber(), user.getPassword(), user.getRole().getRole()))
                .collect(Collectors.toList());
    }


    // update user
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        if (userId == null) {
            throw new InvalidUserDataException("User ID cannot be null");
        }

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidUserDataException("User not found with ID: " + userId));

        if (userDTO.getPhoneNumber() != null && !userDTO.getPhoneNumber().isEmpty()) {
            existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            existingUser.setPassword(encodedPassword);
        }

        if (userDTO.getRole() != null && !userDTO.getRole().isEmpty()) {
            if (!userDTO.getRole().equalsIgnoreCase("USER") && !userDTO.getRole().equalsIgnoreCase("ADMIN")) {
                throw new InvalidUserDataException("Invalid role: Only 'USER' or 'ADMIN' are allowed.");
            }
            Role role = new Role();
            role.setRole(userDTO.getRole().toUpperCase());
            existingUser.setRole(role);
        }

        User updatedUser = userRepository.save(existingUser);
        return new UserDTO(updatedUser.getId(), updatedUser.getPhoneNumber(), updatedUser.getPassword(), updatedUser.getRole().getRole());
    }

    // delete user
    public void deleteUser(Long userId) {
        if (userId == null) {
            throw new InvalidUserDataException("User Id can not be null");
        }
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new InvalidUserDataException("User not found with ID: " + userId);
        }
    }

}