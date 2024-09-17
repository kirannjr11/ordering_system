package table.order.table.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import table.order.table.dto.CartItemDTO;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.service.CartItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;

    @PostMapping
    public CartItemDTO createCartItem(@RequestBody CartItemDTO cartItemDTO, @PathVariable Long cartId) {
        return cartItemService.createCartItem(cartItemDTO, cartId);
    }

    @GetMapping
    public List<CartItemDTO> getAllCartItems(@PathVariable Long cartId) {
        return cartItemService.getAllCartItems(cartId);
    }


    @PutMapping("/{id}")
    public CartItemDTO updateCartItem(@PathVariable Long id, @RequestBody CartItemDTO cartItemDTO) {
        CartItemDTO existingCartItemDto = cartItemService.getCartItemById(id);
        if (existingCartItemDto == null) {
            throw new InvalidUserDataException("Item not found with ID: " + id);
        }
        return cartItemService.updateCartItem(id, cartItemDTO);
    }

    // Delete CartItem
    @DeleteMapping("/{cartItemId}")
    public void deleteCartItem(@PathVariable Long cartItemId, @PathVariable Long cartId) {
        List<CartItemDTO> cartItems = cartItemService.getAllCartItems(cartId);
        boolean cartItemExists = cartItems.stream().anyMatch(item -> item.getId().equals(cartItemId));
        if (!cartItemExists) {
            throw new InvalidUserDataException("CartItem not found with ID: " + cartItemId);
        }
        cartItemService.deleteCartItem(cartItemId);
    }

}
