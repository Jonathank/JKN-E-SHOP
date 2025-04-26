/**
 * 
 */
package nana.shop.online.model;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
import nana.shop.online.domain.AccountStatus;
import nana.shop.online.domain.USER_ROLE;

/**
 * @author JONATHAN
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Seller implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String sellerName;
    private String mobile;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String password;
    
    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();
    @Embedded
    private BankDetails bankDetails = new BankDetails();
    
    @OneToOne(cascade = CascadeType.ALL)
    private Address pickUpAddress = new Address();
    
    private String GSTIN;
    private USER_ROLE role = USER_ROLE.ROLE_SELLER;
    
    private boolean isEmailVerified = false;
    private AccountStatus accountStatus =  AccountStatus.PENDING_VERIFICATION;
    
}
