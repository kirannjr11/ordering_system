package table.order.table.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import table.order.table.dto.CartDTO;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping
    public CartDTO createCart(@RequestBody CartDTO cartDTO) {
        return cartService.createCart(cartDTO);
    }

    @GetMapping
    public List<CartDTO> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/{id}")
    public CartDTO getCartById(@PathVariable Long id) {
        CartDTO cartDTO = cartService.getCartById(id);
        if (cartDTO == null) {
            throw new InvalidUserDataException("Cart not found with ID: " + id);
        }
        return cartDTO;
    }

    @PutMapping("/{id}")
    public CartDTO updateCart(@PathVariable Long id, @RequestBody CartDTO cartDTO) {
        CartDTO existingCartDTO = cartService.getCartById(id);
        if (existingCartDTO == null) {
            throw new InvalidUserDataException("Cart not found with ID: " + id);
        }
        return cartService.updateCart(id, cartDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCart(@PathVariable Long id) {
        CartDTO cartDTO = cartService.getCartById(id);
        if (cartDTO == null) {
            throw new InvalidUserDataException("Cart not found with ID: " + id);
        }
        cartService.deleteCart(id);
    }
}
