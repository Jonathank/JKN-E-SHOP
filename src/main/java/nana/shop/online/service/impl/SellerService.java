/**
 * 
 */
package nana.shop.online.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nana.shop.online.config.JwtProvider;
import nana.shop.online.domain.AccountStatus;
import nana.shop.online.domain.USER_ROLE;
import nana.shop.online.exception.SellerException;
import nana.shop.online.model.Address;
import nana.shop.online.model.Seller;
import nana.shop.online.repositories.AddressRepository;
import nana.shop.online.repositories.SellerRepository;
import nana.shop.online.service.ISellerService;

/**
 * @author JONATHAN
 */
@Service
@RequiredArgsConstructor
public class SellerService implements ISellerService {

    private final JwtProvider jwtProvider;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    
   
    @Override
    public Seller getSellerProfile(String jwt) throws Exception {
	 String email = jwtProvider.getEmailFromToken(jwt);
	 return this.getSellerByEmail(email);
     }

     @Override
     public Seller createSeller(Seller seller) throws Exception {
	 Seller ExistingSeller = sellerRepository.findByEmail(seller.getEmail());
	 if (ExistingSeller != null) {
	     throw new Exception("Seller already exist , use different email");
	 }
	 
	 Address savedAddress = addressRepository.save(seller.getPickUpAddress());
	 Seller newSeller = new Seller();
	 newSeller.setEmail(seller.getEmail());
	 newSeller.setSellerName(seller.getSellerName());
	 newSeller.setPickUpAddress(savedAddress);
	 newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
	 newSeller.setGSTIN(seller.getGSTIN());
	 newSeller.setRole(USER_ROLE.ROLE_SELLER);
	 newSeller.setMobile(seller.getMobile());
	 newSeller.setBankDetails(seller.getBankDetails());
	 newSeller.setBusinessDetails(seller.getBusinessDetails());
	 
	 return sellerRepository.save(newSeller);
     }

  
    

    @Override
    public Seller getSellerById(Long id) throws SellerException {
	
	return sellerRepository	.findById(id)
		.orElseThrow(()-> new SellerException("Seller not found with id : "+id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
	Seller seller = sellerRepository.findByEmail(email);
	if (seller == null) {
	    throw new Exception("Seller not found");
	}
	return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
	
	return sellerRepository.findByAccountStatus(status);
    }


    @Override
    public Seller updateSeller(Seller seller, Long id) throws Exception {
	Seller existingSeller = this.getSellerById(id);
		
	if(seller.getSellerName() != null) {
	    existingSeller.setSellerName(seller.getSellerName());
	}
	if(seller.getMobile()!= null) {
	    existingSeller.setMobile(seller.getMobile());
	}
	if(seller.getEmail() != null) {
	    existingSeller.setEmail(seller.getEmail());
	}
	if(seller.getBusinessDetails() != null) {
	    existingSeller.setBusinessDetails(seller.getBusinessDetails());
	}
	if(seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null) {
	    existingSeller.getBusinessDetails().getBusinessName();
	    seller.getBusinessDetails().getBusinessName();                                                                                    
	}
	if(seller.getBankDetails() != null
		&& seller.getBankDetails().getAccountHolderName() !=null
		&& seller.getBankDetails().getIfscCode() != null
		&& seller.getBankDetails().getAccountNumber() != null
		) {
	    
	    existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
	    existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
	    existingSeller.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());

	}
	
	if(seller.getPickUpAddress() != null
		&& seller.getPickUpAddress().getAddress() != null
		&& seller.getPickUpAddress().getMobile() != null
		&& seller.getPickUpAddress().getCity() != null
		&& seller.getPickUpAddress().getState() != null
		
		) {
	    existingSeller.getPickUpAddress()
	    .setAddress(seller.getPickUpAddress().getAddress());
	    existingSeller.getPickUpAddress()
	    .setCity(seller.getPickUpAddress().getCity());
	    existingSeller.getPickUpAddress()
	    .setState(seller.getPickUpAddress().getState());
	    existingSeller.getPickUpAddress()
	    .setMobile(seller.getPickUpAddress().getMobile());
	    existingSeller.getPickUpAddress()
	    .setPinCode(seller.getPickUpAddress().getPinCode());
	    
	}
	
	if(seller.getGSTIN() != null) {
	    existingSeller.setGSTIN(seller.getGSTIN());
	}
	
	return sellerRepository.save(existingSeller);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {
	Seller seller = this.getSellerById(id);
	sellerRepository.delete(seller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
	Seller seller = getSellerByEmail(email);
	seller.setEmailVerified(true);
	
	return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {
	Seller seller = this.getSellerById(sellerId);
	seller.setAccountStatus(status);
	
	return sellerRepository.save(seller);
    }

}
