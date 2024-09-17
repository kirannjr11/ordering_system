package table.order.table.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import table.order.table.dto.CartItemDTO;
import table.order.table.dto.MenuDTO;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.model.Cart;
import table.order.table.model.CartItem;
import table.order.table.model.Menu;
import table.order.table.repository.CartItemRepository;
import table.order.table.repository.CartRepository;
import table.order.table.repository.MenuRepository;

import java.util.List;
import java.util.stream.Collectors;



@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MenuRepository menuRepository;

    // Create CartItem
    public CartItemDTO createCartItem(CartItemDTO cartItemDTO, Long cartId) {
        if (cartId == null) {
            throw new InvalidUserDataException("Cart ID cannot be null");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new InvalidUserDataException("Cart not found with ID: " + cartId));

        // Check if MenuDTO is null
        if (cartItemDTO.getMenu() == null || cartItemDTO.getMenu().getId() == null) {
            throw new InvalidUserDataException("Menu information is required and cannot be null");
        }

        // Convert MenuDTO to Menu entity directly
        Menu menu = menuRepository.findById(cartItemDTO.getMenu().getId())
                .orElseThrow(() -> new InvalidUserDataException("Menu not found with ID: " + cartItemDTO.getMenu().getId()));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setMenu(menu);
        cartItem.setQuantity(cartItemDTO.getQuantity());

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        MenuDTO menuDTO = new MenuDTO(menu.getId(), menu.getName(), menu.getPrice(), menu.getDescription());
        return new CartItemDTO(savedCartItem.getId(), menuDTO, savedCartItem.getQuantity());
    }

    public CartItemDTO getCartItemById(Long cartItemId) {
        if (cartItemId == null) {
            throw new InvalidUserDataException("CartItem ID cannot be null");
        }


        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new InvalidUserDataException("CartItem not found with ID: " + cartItemId));


        Menu menu = cartItem.getMenu();
        MenuDTO menuDTO = new MenuDTO(menu.getId(), menu.getName(), menu.getPrice(), menu.getDescription());
        return new CartItemDTO(cartItem.getId(), menuDTO, cartItem.getQuantity());
    }

    // Get all CartItems
    public List<CartItemDTO> getAllCartItems(Long cartId) {
        if (cartId == null) {
            throw new InvalidUserDataException("Cart ID cannot be null");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new InvalidUserDataException("Cart not found with ID: " + cartId));

        return cart.getCartItems().stream()
                .map(cartItem -> {
                    Menu menu = cartItem.getMenu();
                    MenuDTO menuDTO = new MenuDTO(menu.getId(), menu.getName(), menu.getPrice(), menu.getDescription());
                    return new CartItemDTO(cartItem.getId(), menuDTO, cartItem.getQuantity());
                })
                .collect(Collectors.toList());
    }


    // Update CartItem
    public CartItemDTO updateCartItem(Long cartItemId, CartItemDTO cartItemDTO) {
        CartItem existingCartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new InvalidUserDataException("CartItem not found with ID: " + cartItemId));

        if (cartItemDTO.getQuantity() != null) {
            existingCartItem.setQuantity(cartItemDTO.getQuantity());
        }

        if (cartItemDTO.getMenu() != null) {
            Menu menu = menuRepository.findById(cartItemDTO.getMenu().getId())
                    .orElseThrow(() -> new InvalidUserDataException("Menu not found with ID: " + cartItemDTO.getMenu().getId()));
            existingCartItem.setMenu(menu);
        }

        CartItem updatedCartItem = cartItemRepository.save(existingCartItem);
        MenuDTO menuDTO = new MenuDTO(updatedCartItem.getMenu().getId(), updatedCartItem.getMenu().getName(), updatedCartItem.getMenu().getPrice(), updatedCartItem.getMenu().getDescription());
        return new CartItemDTO(updatedCartItem.getId(), menuDTO, updatedCartItem.getQuantity());
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
