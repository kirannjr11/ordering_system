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

        // Ensure the status is provided
        if (orderDTO.getStatus() == null) {
            throw new InvalidUserDataException("Order status cannot be null.");
        }

        // Set the status directly, since it's already an enum
        order.setStatus(orderDTO.getStatus());

        Order savedOrder = orderRepository.save(order);

        // Return the saved order DTO
        return new OrderDTO(
                savedOrder.getId(),
                savedOrder.getUser().getId(),
                savedOrder.getTableId(),
                savedOrder.getStatus(),  // Passing the enum directly
                savedOrder.getCreatedAt(),
                null
        );
    }

    // Get order by id
    public OrderDTO getOrderById(Long orderId) {
        if (orderId == null) {
            throw new InvalidUserDataException("Order ID can't be null");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidUserDataException("Order not found with ID: " + orderId));

        // Return the order DTO
        return new OrderDTO(
                order.getId(),
                order.getUser().getId(),
                order.getTableId(),
                order.getStatus(),  // Passing the enum directly
                order.getCreatedAt(),
                null
        );
    }

    // Get all Orders
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getUser().getId(),
                        order.getTableId(),
                        order.getStatus(),  // Passing the enum directly
                        order.getCreatedAt(),
                        null))
                .collect(Collectors.toList());
    }

    // Update order
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
        if (orderId == null) {
            throw new InvalidUserDataException("Order ID cannot be null");
        }

        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidUserDataException("Order not found with ID: " + orderId));

        if (orderDTO.getTableId() != null) {
            existingOrder.setTableId(orderDTO.getTableId());
        }

        // Ensure the status is provided for update
        if (orderDTO.getStatus() != null) {
            existingOrder.setStatus(orderDTO.getStatus());  // Set the enum directly
        }

        Order updatedOrder = orderRepository.save(existingOrder);

        // Return the updated order DTO
        return new OrderDTO(
                updatedOrder.getId(),
                updatedOrder.getUser().getId(),
                updatedOrder.getTableId(),
                updatedOrder.getStatus(),  // Passing the enum directly
                updatedOrder.getCreatedAt(),
                null
        );
    }

    // Get Orders by Table ID
    public List<OrderDTO> getOrdersByTableId(Long tableId) {
        if (tableId == null) {
            throw new InvalidUserDataException("Table ID cannot be null.");
        }

        List<Order> orders = orderRepository.findByTableId(tableId);
        if (orders.isEmpty()) {
            throw new InvalidUserDataException("No orders found for Table ID: " + tableId);
        }

        return orders.stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getUser().getId(),
                        order.getTableId(),
                        order.getStatus(),  // Passing the enum directly
                        order.getCreatedAt(),
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

