/**
 * 
 */
package nana.shop.online.service;

import java.util.List;

import nana.shop.online.domain.AccountStatus;
import nana.shop.online.exception.SellerException;
import nana.shop.online.model.Seller;

/**
 *@author JONATHAN 
 */
public interface ISellerService {
    
    Seller getSellerProfile(String jwt) throws Exception;
    Seller createSeller(Seller seller) throws Exception;
    Seller getSellerById(Long id) throws SellerException;
    Seller getSellerByEmail(String email) throws Exception;
    List<Seller>getAllSellers(AccountStatus status);
    Seller updateSeller(Seller seller,Long id) throws Exception;
    void deleteSeller(Long id) throws Exception;
    Seller verifyEmail(String email, String otp) throws Exception;
    Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception;
    
}
