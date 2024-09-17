package table.order.table.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import table.order.table.Enum.Role;
import table.order.table.dto.UserDTO;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.model.User;;
import table.order.table.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service

public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // create user
    public UserDTO createUser(UserDTO userDTO) {
        // Validate input
        if (userDTO.getPhoneNumber() == null || userDTO.getPhoneNumber().isEmpty()) {
            throw new InvalidUserDataException("Phone number cannot be null");
        }

        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new InvalidUserDataException("Password cannot be null or empty");
        }

        // Convert the string role to enum
        Role role;
        try {
            role = Role.valueOf(userDTO.getRole().name()); // Convert string role to enum
        } catch (IllegalArgumentException e) {
            throw new InvalidUserDataException("Invalid role: Only 'USER' or 'ADMIN' are allowed.");
        }

        // Create and save the user
        User user = new User();
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(role);  // Set the role enum

        User savedUser = userRepository.save(user);
        System.out.println("User saved with ID: " + savedUser.getId());

        // Pass the Role enum directly to the DTO
        return new UserDTO(savedUser.getId(), savedUser.getPhoneNumber(), savedUser.getPassword(), savedUser.getRole());
    }

    // Get user by id
    public UserDTO getUserById(Long userId) {
        if (userId == null) {
            throw new InvalidUserDataException("User id cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidUserDataException("User not found with id: " + userId));

        // Pass the Role enum directly to the DTO
        return new UserDTO(user.getId(), user.getPhoneNumber(), user.getPassword(), user.getRole());
    }

    // Get user by phone number
    public UserDTO getUserByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new InvalidUserDataException("Phone number cannot be null or empty");
        }

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new InvalidUserDataException("User not found with phone number: " + phoneNumber));

        // Pass the Role enum directly to the DTO
        return new UserDTO(user.getId(), user.getPhoneNumber(), user.getPassword(), user.getRole());
    }

    // List all users
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getPhoneNumber(), user.getPassword(), user.getRole()))
                .collect(Collectors.toList());
    }

    // Update user
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

        if (userDTO.getRole() != null) {
            existingUser.setRole(userDTO.getRole());
        }

        User updatedUser = userRepository.save(existingUser);
        return new UserDTO(updatedUser.getId(), updatedUser.getPhoneNumber(), updatedUser.getPassword(), updatedUser.getRole());
    }

    // Delete user
    public void deleteUser(Long userId) {
        if (userId == null) {
            throw new InvalidUserDataException("User ID cannot be null");
        }

        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new InvalidUserDataException("User not found with ID: " + userId);
        }
    }

}