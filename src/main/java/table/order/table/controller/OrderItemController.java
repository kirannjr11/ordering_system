package table.order.table.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import table.order.table.dto.OrderItemDTO;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.service.OrderItemService;

import java.util.List;

@RestController
@RequestMapping("/orders/{orderId}/items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @PostMapping
    public OrderItemDTO createOrderItem(@RequestBody OrderItemDTO orderItemDTO, @PathVariable Long orderId) {
        return orderItemService.createOrderItem(orderItemDTO, orderId);
    }

    @GetMapping("/{id}")
    public OrderItemDTO getOrderItemById(@PathVariable Long id) {
        OrderItemDTO orderItemDTO = orderItemService.getOrderItemById(id);
        if (orderItemDTO == null) {
            throw new InvalidUserDataException("OrderItem not found with ID: " + id);
        }
        return orderItemDTO;
    }

    @GetMapping
    public List<OrderItemDTO> getOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderItemService.getOrderItemsByOrderId(orderId);
    }

    @PutMapping("/{id}")
    public OrderItemDTO updateOrderItem(@PathVariable Long id, @RequestBody OrderItemDTO orderItemDTO) {
        OrderItemDTO existingOrderItemDTO = orderItemService.getOrderItemById(id);
        if (existingOrderItemDTO == null) {
            throw new InvalidUserDataException("OrderItem not found with ID: " + id);
        }
        return orderItemService.updateOrderItem(id, orderItemDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteOrderItem(@PathVariable Long id) {
        OrderItemDTO orderItemDTO = orderItemService.getOrderItemById(id);
        if (orderItemDTO == null) {
            throw new InvalidUserDataException("OrderItem not found with ID: " + id);
        }
        orderItemService.deleteOrderItem(id);
    }

}
