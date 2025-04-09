/**
 * 
 */
package nana.shop.online.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.model.Cart;

/**
 * @author JONATHAN
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * @param id
     * @return
     */
    Cart findUserById(Long id);

}
