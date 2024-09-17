package table.order.table.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import table.order.table.dto.OrderDTO;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public OrderDTO createOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.createOrder(orderDTO, orderDTO.getUserId());  // Ensure this method is correctly mapped
    }

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        if (orderDTO == null) {
            throw new InvalidUserDataException("Order not found with ID: " + id);
        }
        return orderDTO;
    }

    @GetMapping("/table/{tableId}")
    public List<OrderDTO> getOrdersByTableId(@PathVariable Long tableId) {
        return orderService.getOrdersByTableId(tableId);
    }
    @PutMapping("/{id}")
    public OrderDTO updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        OrderDTO existingOrderDTO = orderService.getOrderById(id);
        if (existingOrderDTO == null) {
            throw new InvalidUserDataException("Order not found with ID: " + id);
        }
        return orderService.updateOrder(id, orderDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
