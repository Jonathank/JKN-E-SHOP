/**
 * 
 */
package nana.shop.online.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.model.SellerReport;

/**
 * @author JONATHAN
 */
public interface SellerReportRepository extends JpaRepository<SellerReport, Long>{

    /**
     * @param id
     * @return
     */
    SellerReport findBySellerId(Long id);

}
