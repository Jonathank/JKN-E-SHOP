/**
 * 
 */
package nana.shop.online.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nana.shop.online.domain.OrderStatus;
import nana.shop.online.domain.PaymentStatus;

/**
 * @author JONATHAN
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String orderId;
    
    @ManyToOne
    private User user;
    private Long sellerId;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItems>orderItems = new ArrayList<>();
    
    @ManyToOne
    private Address shippingAddress;
    
    @Embedded
    private PaymentDetails paymentDetails = new PaymentDetails();
    
    private double totalMrpPrice;
     private Integer discount;
     private OrderStatus orderStatus;
     private int totalItems;
     private PaymentStatus paymentStatus = PaymentStatus.PENDING;
     
     private LocalDateTime orderDate = LocalDateTime.now();
     private LocalDateTime deliverDate = orderDate.plusDays(7);
}
