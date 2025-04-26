/**
 * 
 */
package nana.shop.online.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.model.Coupon;

/**
 * @author JONATHAN
 */
public interface CouponRepository extends JpaRepository<Coupon, Long>{

    /**
     * @param code
     * @return
     */
    Coupon findBycode(String code);

    
}
