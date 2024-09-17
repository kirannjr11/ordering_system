package table.order.table.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import table.order.table.Enum.OrderStatus;
import table.order.table.dto.OrderDTO;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.model.Order;
import table.order.table.model.User;
import table.order.table.repository.OrderRepository;
import table.order.table.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // Create Order
    public OrderDTO createOrder(OrderDTO orderDTO, Long userId) {
        if (userId == null) {
            throw new InvalidUserDataException("User ID cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidUserDataException("User not found with ID: " + userId));

        Order order = new Order();
        order.setUser(user);
        order.setTableId(orderDTO.getTableId());

        try {
            OrderStatus status = OrderStatus.valueOf(orderDTO.getStatus());  // Matches "CANCEL" or "COMPLETE"
            order.setStatus(status);
        } catch (IllegalArgumentException e) {
            throw new InvalidUserDataException("Invalid status value: " + orderDTO.getStatus());
        }

        Order savedOrder = orderRepository.save(order);
        return new OrderDTO(savedOrder.getId(), savedOrder.getUser().getId(), savedOrder.getTableId(), savedOrder.getStatus().name(), null);
    }

    // Get all Orders
    // Get all Orders
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getUser().getId(),
                        order.getTableId(),
                        order.getStatus().name(),  // Convert enum to String
                        null))
                .collect(Collectors.toList());
    }

    // Delete Order
    public void deleteOrder(Long orderId) {
        if (orderId == null) {
            throw new InvalidUserDataException("Order ID cannot be null");
        }

        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            throw new InvalidUserDataException("Order not found with ID: " + orderId);
        }
    }
}
