package table.order.table.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import table.order.table.dto.CartItemDTO;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.model.Cart;
import table.order.table.model.CartItem;
import table.order.table.repository.CartItemRepository;
import table.order.table.repository.CartRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    // Create CartItem
    public CartItemDTO createCartItem(CartItemDTO cartItemDTO, Long cartId) {
        if (cartId == null) {
            throw new InvalidUserDataException("Cart ID cannot be null");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new InvalidUserDataException("Cart not found with ID: " + cartId));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setMenu(cartItemDTO.getMenu());
        cartItem.setQuantity(cartItemDTO.getQuantity());

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return new CartItemDTO(savedCartItem.getId(), savedCartItem.getMenu(), savedCartItem.getQuantity());
    }

    // Get all CartItems
    public List<CartItemDTO> getAllCartItems(Long cartId) {
        if (cartId == null) {
            throw new InvalidUserDataException("Cart ID cannot be null");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new InvalidUserDataException("Cart not found with ID: " + cartId));

        return cart.getCartItems().stream()
                .map(cartItem -> new CartItemDTO(cartItem.getId(), cartItem.getMenu(), cartItem.getQuantity()))
                .collect(Collectors.toList());
    }

    // Delete CartItem
    public void deleteCartItem(Long cartItemId) {
        if (cartItemId == null) {
            throw new InvalidUserDataException("CartItem ID cannot be null");
        }

        if (cartItemRepository.existsById(cartItemId)) {
            cartItemRepository.deleteById(cartItemId);
        } else {
            throw new InvalidUserDataException("CartItem not found with ID: " + cartItemId);
        }
    }

}
