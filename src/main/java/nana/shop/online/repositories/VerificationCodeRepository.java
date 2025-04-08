/**
 * 
 */
package nana.shop.online.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.model.VerificationCode;

/**
 * @author JONATHAN
 */
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByEmail(String userEmail);

    /**
     * @param otp
     * @return
     */
    VerificationCode findByOtp(String otp);

    //void deleteByUserEmail(String userEmail);

}
