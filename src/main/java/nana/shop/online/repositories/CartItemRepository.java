/**
 * 
 */
package nana.shop.online.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nana.shop.online.model.CartItems;

/**
 * @author JONATHAN
 */
public interface CartItemRepository extends JpaRepository<CartItems, Long> {
    
    @Query("SELECT c FROM CartItem c WHERE c.cart.id = :cartId")
    List<CartItems> findByCartId(@Param("cartId") Long cartId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.cart.id = :cartId")
    void deleteByCartId(@Param("cartId") Long cartId);

}
