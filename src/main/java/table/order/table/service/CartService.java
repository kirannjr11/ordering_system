package table.order.table.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import table.order.table.dto.CartDTO;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.model.Cart;
import table.order.table.model.User;
import table.order.table.repository.CartRepository;
import table.order.table.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    // Create Cart
    public CartDTO createCart(Long userId) {
        if (userId == null) {
            throw new InvalidUserDataException("User ID cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidUserDataException("User not found with ID: " + userId));

        Cart cart = new Cart();
        cart.setUser(user);

        Cart savedCart = cartRepository.save(cart);
        return new CartDTO(savedCart.getId(), savedCart.getUser().getId(), null);
    }

    // Get Cart by ID
    public CartDTO getCartById(Long cartId) {
        if (cartId == null) {
            throw new InvalidUserDataException("Cart ID cannot be null");
        }
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new InvalidUserDataException("Cart not found with ID: " + cartId));

        return new CartDTO(cart.getId(), cart.getUser().getId(), null);
    }

    // Get all Carts
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        return carts.stream()
                .map(cart -> new CartDTO(cart.getId(), cart.getUser().getId(), null))
                .collect(Collectors.toList());
    }

    // Delete Cart
    public void deleteCart(Long cartId) {
        if (cartId == null) {
            throw new InvalidUserDataException("Cart ID cannot be null");
        }

        if (cartRepository.existsById(cartId)) {
            cartRepository.deleteById(cartId);
        } else {
            throw new InvalidUserDataException("Cart not found with ID: " + cartId);
        }
    }
}
