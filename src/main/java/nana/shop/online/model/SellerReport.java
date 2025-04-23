/**
 * 
 */
package nana.shop.online.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author JONATHAN
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class SellerReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    private Seller seller;
    
    private double totalEarnings = 0L;
    private double totalSales = 0L;
    private double totalRefunds = 0L;
    private double totalTax = 0L;
    private double netEarnings = 0L;
   
    private Integer totalOrders = 0;
    private Integer canceledOrders = 0;
    private Integer totalTransactions = 0;
    
}
