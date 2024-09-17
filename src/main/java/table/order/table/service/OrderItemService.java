package table.order.table.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import table.order.table.dto.MenuDTO;
import table.order.table.dto.OrderItemDTO;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.model.Menu;
import table.order.table.model.Order;
import table.order.table.model.OrderItem;
import table.order.table.repository.MenuRepository;
import table.order.table.repository.OrderItemRepository;
import table.order.table.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    // Create OrderItem
    public OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO, Long orderId) {
        if (orderId == null) {
            throw new InvalidUserDataException("Order ID cannot be null");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidUserDataException("Order not found with ID: " + orderId));

        Menu menu = menuRepository.findById(orderItemDTO.getMenu().getId())
                .orElseThrow(() -> new InvalidUserDataException("Menu item not found with ID: " + orderItemDTO.getMenu().getId()));

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenu(menu);
        orderItem.setQuantity(orderItemDTO.getQuantity());

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        MenuDTO menuDTO = new MenuDTO(menu.getId(), menu.getName(), menu.getPrice(), menu.getDescription());
        return new OrderItemDTO(savedOrderItem.getId(), menuDTO, savedOrderItem.getQuantity());
    }

    // Get OrderItem by ID
    public OrderItemDTO getOrderItemById(Long orderItemId) {
        if (orderItemId == null) {
            throw new InvalidUserDataException("OrderItem ID cannot be null");
        }

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new InvalidUserDataException("OrderItem not found with ID: " + orderItemId));

        MenuDTO menuDTO = new MenuDTO(orderItem.getMenu().getId(), orderItem.getMenu().getName(), orderItem.getMenu().getPrice(), orderItem.getMenu().getDescription());
        return new OrderItemDTO(orderItem.getId(), menuDTO, orderItem.getQuantity());
    }

    // List all OrderItems for a given Order
    public List<OrderItemDTO> getOrderItemsByOrderId(Long orderId) {
        if (orderId == null) {
            throw new InvalidUserDataException("Order ID cannot be null");
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        // Convert OrderItem entities to OrderItemDTOs
        return orderItems.stream()
                .map(orderItem -> {
                    MenuDTO menuDTO = new MenuDTO(orderItem.getMenu().getId(), orderItem.getMenu().getName(), orderItem.getMenu().getPrice(), orderItem.getMenu().getDescription());
                    return new OrderItemDTO(orderItem.getId(), menuDTO, orderItem.getQuantity());
                })
                .collect(Collectors.toList());
    }


    // Update OrderItem
    public OrderItemDTO updateOrderItem(Long orderItemId, OrderItemDTO orderItemDTO) {
        if (orderItemId == null) {
            throw new InvalidUserDataException("OrderItem ID cannot be null");
        }

        OrderItem existingOrderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new InvalidUserDataException("OrderItem not found with ID: " + orderItemId));

        if (orderItemDTO.getQuantity() > 0) {
            existingOrderItem.setQuantity(orderItemDTO.getQuantity());
        }

        if (orderItemDTO.getMenu() != null) {
            Menu menu = menuRepository.findById(orderItemDTO.getMenu().getId())
                    .orElseThrow(() -> new InvalidUserDataException("Menu item not found with ID: " + orderItemDTO.getMenu().getId()));
            existingOrderItem.setMenu(menu);
        }

        OrderItem updatedOrderItem = orderItemRepository.save(existingOrderItem);

        MenuDTO menuDTO = new MenuDTO(updatedOrderItem.getMenu().getId(), updatedOrderItem.getMenu().getName(), updatedOrderItem.getMenu().getPrice(), updatedOrderItem.getMenu().getDescription());
        return new OrderItemDTO(updatedOrderItem.getId(), menuDTO, updatedOrderItem.getQuantity());
    }

    // Delete OrderItem
    public void deleteOrderItem(Long orderItemId) {
        if (orderItemId == null) {
            throw new InvalidUserDataException("OrderItem ID cannot be null");
        }

        if (orderItemRepository.existsById(orderItemId)) {
            orderItemRepository.deleteById(orderItemId);
        } else {
            throw new InvalidUserDataException("OrderItem not found with ID: " + orderItemId);
        }
    }
}
