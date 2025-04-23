/**
 * 
 */
package nana.shop.online.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.model.WishList;

/**
 * @author JONATHAN
 */
public interface WishListRepository extends JpaRepository<WishList, Long>{

    /**
     * @param id
     * @return
     */
    WishList findByUserId(Long id);

}
