/**
 * 
 */
package nana.shop.online.service;

import java.util.List;
import java.util.Set;

import nana.shop.online.domain.OrderStatus;
import nana.shop.online.model.Address;
import nana.shop.online.model.Cart;
import nana.shop.online.model.Order;
import nana.shop.online.model.OrderItems;
import nana.shop.online.model.User;

/**
 * @author JONATHAN
 */
public interface IOrderService {

    Set<Order> createOrder(User user,Address shippingAddress, Cart cart);
    Order findOrderById(Long orderId);
    List<Order> usersOrderHistory(Long userId);
    List<Order> sellersOrders(Long sellerId);
    Order updateOrderStatus(Long orderId,OrderStatus orderStatus);
    Order cancelOrder(Long orderId,User user);
    OrderItems getOrderItemById(Long orderItemId);
}
