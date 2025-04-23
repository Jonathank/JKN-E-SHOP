/**
 * 
 */
package nana.shop.online.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.model.PaymentOrder;

/**
 * @author JONATHAN
 */
public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long>{

    /**
     * @param paymentId
     * @return
     */
    PaymentOrder findByPaymentLinkId(String paymentId);

}
