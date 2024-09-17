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
        return new OrderDTO(savedOrder.getId(), savedOrder.getUser().getId(), savedOrder.getTableId(), savedOrder.getStatus().name(), savedOrder.getCreatedAt(), null);
    }

    // get order by id
    public OrderDTO getOrderById(Long orderId) {
        if (orderId == null) {
            throw new InvalidUserDataException("Order id can't be null");
        }

        Order order = orderRepository.findById(orderId).orElseThrow(()-> new InvalidUserDataException("Order not found with id :"+orderId));
        return new OrderDTO(order.getId(), order.getUser().getId(), order.getTableId(), order.getStatus().name(), order.getCreatedAt(), null);
    }

    // Get all Orders
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getUser().getId(),
                        order.getTableId(),
                        order.getStatus().name(),
                        order.getCreatedAt(),  // Pass the createdAt field
                        null))
                .collect(Collectors.toList());
    }

    // update order
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
        if (orderId == null) {
            throw new InvalidUserDataException("Order ID cannot be null");
        }

        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidUserDataException("Order not found with ID: " + orderId));

        if (orderDTO.getTableId() != null) {
            existingOrder.setTableId(orderDTO.getTableId());
        }

        if (orderDTO.getStatus() != null && !orderDTO.getStatus().isEmpty()) {
            OrderStatus status = OrderStatus.valueOf(orderDTO.getStatus().toUpperCase());
            if (status == null) {
                throw new InvalidUserDataException("Invalid status value: " + orderDTO.getStatus());
            }
            existingOrder.setStatus(status);
        }

        Order updatedOrder = orderRepository.save(existingOrder);
        return new OrderDTO(updatedOrder.getId(), updatedOrder.getUser().getId(), updatedOrder.getTableId(), updatedOrder.getStatus().name(), updatedOrder.getCreatedAt(), null);
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
                        order.getStatus().name(),
                        order.getCreatedAt(),  // Pass the createdAt field
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
