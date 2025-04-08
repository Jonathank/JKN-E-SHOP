/**
 * 
 */
package nana.shop.online.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.domain.AccountStatus;
import nana.shop.online.model.Seller;

/**
 * @author JONATHAN
 */
public interface SellerRepository extends JpaRepository<Seller, Long> {

   Seller findByEmail(String email);

/**
 * @param status
 * @return
 */
List<Seller> findByAccountStatus(AccountStatus status);

}
