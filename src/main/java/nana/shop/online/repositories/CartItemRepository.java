/**
 * 
 */
package nana.shop.online.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.model.Cart;
import nana.shop.online.model.CartItems;
import nana.shop.online.model.Product;

/**
 * @author JONATHAN
 */
public interface CartItemRepository extends JpaRepository<CartItems, Long> {

    /**
     * @param cart
     * @param product
     * @param size
     * @return
     */
    CartItems findByCartAndProductAndSize(Cart cart, Product product, String size);

}
