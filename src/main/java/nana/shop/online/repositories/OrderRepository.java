/**
 * 
 */
package nana.shop.online.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.model.Order;

/**
 * @author JONATHAN
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

     List<Order> findByUserId(Long userId);
//
     List<Order> findBySellerId(Long sellerId);
//
//    List<Order> findByStatus(OrderStatus status);

}
