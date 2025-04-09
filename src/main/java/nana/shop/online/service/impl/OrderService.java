/**
 * 
 */
package nana.shop.online.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nana.shop.online.domain.OrderStatus;
import nana.shop.online.domain.PaymentStatus;
import nana.shop.online.exception.OrderException;
import nana.shop.online.model.Address;
import nana.shop.online.model.Cart;
import nana.shop.online.model.CartItems;
import nana.shop.online.model.Order;
import nana.shop.online.model.OrderItems;
import nana.shop.online.model.User;
import nana.shop.online.repositories.AddressRepository;
import nana.shop.online.repositories.OrderItemRepository;
import nana.shop.online.repositories.OrderRepository;
import nana.shop.online.service.IOrderService;

/**
 * @author JONATHAN
 */
@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    @Override
    /**
     * Creates orders for items in the cart, grouping them by seller.
     * Each seller's items will be processed as a separate order.
     * 
     * @param user The user placing the order
     * @param shippingAddress The address where the order will be shipped
     * @param cart The shopping cart containing items to be ordered
     * @return Set of created orders
     */
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        // Add shipping address to user's addresses if not already present
        if(!user.getAddresses().contains(shippingAddress)) {
            user.getAddresses().add(shippingAddress);
        }
        // Save and persist the shipping address
        Address address = addressRepository.save(shippingAddress);
        
        // Group cart items by seller ID for processing separate orders per seller
        Map<Long, List<CartItems>> itemsBySeller = cart.getCartItems()
            .stream()
            .collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));
        Set<Order> orders = new HashSet<>();
        
        // Process each seller's items as a separate order
        for (Map.Entry<Long, List<CartItems>> entry : itemsBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItems> items = entry.getValue();

            // Calculate total order price by summing selling prices of all items
            int totalOrderPrice = items.stream()
                .mapToInt(CartItems::getSellingPrice).sum();    
            
            // Calculate total number of items in the order
            int totalItems = items.stream()
                .mapToInt(CartItems::getQuantity).sum();    
            
            // Create and initialize new order
            Order order = new Order();
            order.setUser(user);
            order.setSellerId(sellerId);
            order.setTotalMrpPrice(totalOrderPrice);
            order.setTotalSellingPrice(totalOrderPrice);
            order.setTotalItems(totalItems);
            order.setShippingAddress(address);
            order.setOrderStatus(OrderStatus.PENDING);
            order.getPaymentDetails().setStatus(PaymentStatus.PENDING);    
            
            // Save the order and add it to the set of orders
            Order savedOrder = orderRepository.save(order);
            orders.add(savedOrder);
            
            List<OrderItems> orderItems = new ArrayList<>();
            
            // Process each item in the cart for this seller
            for (CartItems cartItem : items) {
                // Create order item from cart item
                OrderItems orderItem = new OrderItems();
                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(cartItem.getMrpPrice());
                orderItem.setSellingPrice(cartItem.getSellingPrice());
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setSize(cartItem.getSize());
                orderItem.setUserId(cartItem.getUserId());
                
                // Add order item to the order and save it
                savedOrder.getOrderItems().add(orderItem);
                OrderItems savedOrderItem = orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);
            }
        }
        
        return orders;
    }
    @Override
    public Order findOrderById(Long orderId) throws OrderException {
	return orderRepository.findById(orderId)
		.orElseThrow(() -> new OrderException("Order not found"));
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
	
	return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellersOrders(Long sellerId) {
	
	return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
	Order order = findOrderById(orderId);
	order.setOrderStatus(orderStatus);
	return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) {
	Order order = findOrderById(orderId);
	// Check if the order belongs to the user
	if (!user.getId().equals(order.getUser().getId())) {
	    throw new OrderException("You are not authorized to cancel this order");
	}
	order.setOrderStatus(OrderStatus.CANCELLED);
	return orderRepository.save(order);    }
    
    @Override
    public OrderItems findById(Long orderItemId) {
	     // Find the order item by its ID
        return orderItemRepository.findById(orderItemId)
            .orElseThrow(() -> new OrderException("Order item not found"));
    }
	
    

}
