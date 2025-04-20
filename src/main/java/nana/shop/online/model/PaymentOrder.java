/**
 * 
 */
package nana.shop.online.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nana.shop.online.domain.PaymentMethod;
import nana.shop.online.domain.PaymentOrderStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Double amount;
    
    private PaymentOrderStatus status = PaymentOrderStatus.PENDING;
    
    private PaymentMethod paymentMethod;
    private String paymentLinkId;
    
    @ManyToOne
    private User user;
    
    @OneToMany
    private Set<Order>orders = new HashSet<>();
}
