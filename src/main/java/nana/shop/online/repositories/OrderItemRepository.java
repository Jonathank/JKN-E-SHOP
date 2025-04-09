/**
 * 
 */
package nana.shop.online.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.model.OrderItems;

/**
 * @author JONATHAN
 */
public interface OrderItemRepository extends JpaRepository<OrderItems, Long> {

    //List<OrderItems> findByOrderId(Long orderId);

}
