package table.order.table.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import table.order.table.dto.CartItemDTO;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.service.CartItemService;

import java.util.List;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    // Create a new CartItem
    @PostMapping("/carts/{cartId}")
    public CartItemDTO createCartItem(@RequestBody CartItemDTO cartItemDTO, @PathVariable Long cartId) {
        System.out.println("CartItemDTO: " + cartItemDTO); // Log the incoming data
        return cartItemService.createCartItem(cartItemDTO, cartId);
    }



    // Get a CartItem by its ID
    @GetMapping("/{cartItemId}")
    public CartItemDTO getCartItemById(@PathVariable Long cartItemId) {
        CartItemDTO cartItemDTO = cartItemService.getCartItemById(cartItemId);
        if (cartItemDTO == null) {
            throw new InvalidUserDataException("CartItem not found with ID: " + cartItemId);
        }
        return cartItemDTO;
    }

    // Get all CartItems for a Cart by Cart ID
    @GetMapping("/carts/{cartId}")
    public List<CartItemDTO> getAllCartItems(@PathVariable Long cartId) {
        return cartItemService.getAllCartItems(cartId);
    }

    // Update an existing CartItem
    @PutMapping("/{cartItemId}")
    public CartItemDTO updateCartItem(@PathVariable Long cartItemId, @RequestBody CartItemDTO cartItemDTO) {
        CartItemDTO existingCartItemDTO = cartItemService.getCartItemById(cartItemId);
        if (existingCartItemDTO == null) {
            throw new InvalidUserDataException("CartItem not found with ID: " + cartItemId);
        }
        return cartItemService.updateCartItem(cartItemId, cartItemDTO);
    }

    // Delete a CartItem by its ID
    @DeleteMapping("/{cartItemId}")
    public void deleteCartItem(@PathVariable Long cartItemId) {
        CartItemDTO cartItemDTO = cartItemService.getCartItemById(cartItemId);
        if (cartItemDTO == null) {
            throw new InvalidUserDataException("CartItem not found with ID: " + cartItemId);
        }
        cartItemService.deleteCartItem(cartItemId);
    }
}
