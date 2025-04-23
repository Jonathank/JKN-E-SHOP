/**
 * 
 */
package nana.shop.online.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.model.Review;

/**
 * @author JONATHAN
 */
public interface ReviewRepository extends JpaRepository<Review, Long>{

    /**
     * @param productId
     * @return
     */
    List<Review> findByProductId(Long productId);

}
