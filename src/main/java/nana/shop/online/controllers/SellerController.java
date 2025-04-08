/**
 * 
 */
package nana.shop.online.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import nana.shop.online.domain.AccountStatus;
import nana.shop.online.exception.SellerException;
import nana.shop.online.model.Seller;
import nana.shop.online.model.VerificationCode;
import nana.shop.online.repositories.VerificationCodeRepository;
import nana.shop.online.request.LoginRequest;
import nana.shop.online.response.AuthResponse;
import nana.shop.online.service.impl.AuthService;
import nana.shop.online.service.impl.EmailService;
import nana.shop.online.service.impl.SellerService;
import nana.shop.online.utils.OtpUtils;

/**
 * @author JONATHAN
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/seller")
public class SellerController {
    
    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final EmailService emailService;
    //private final JwtProvider jwtProvider;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse>loginSeller (@RequestBody LoginRequest request)throws Exception{
	String otp = request.getOtp();
	String email = request.getEmail();
	
	request.setEmail("seller_"+email);
	System.out.println("otp: "+ otp+" and email : "+email);
	
	AuthResponse response = authService.signin(request);
	
	return ResponseEntity.ok(response);
	
    }
    
    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller>verifySellerEmail (
	    @PathVariable String otp)throws Exception{
   	VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
   	System.out.println("the verification code : "+verificationCode.getOtp());
   	if(verificationCode == null || !verificationCode.getOtp().equals(otp) ) {
   	    throw new Exception("wrong otp...");
   	}
   	Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);
   	return ResponseEntity.ok(seller);
   	
       }
    
    @PostMapping("/new/add")
    public ResponseEntity<Seller>createSeller (
	    @RequestBody Seller seller)throws Exception, MessagingException{
	Seller savedSeller = sellerService.createSeller(seller);
	
	String otp = OtpUtils.generateOtp();
	
	VerificationCode verificationCode = new VerificationCode();
	verificationCode.setEmail(seller.getEmail());
	verificationCode.setOtp(otp);
	
	verificationCodeRepository.save(verificationCode);
	
	 //send otp to user eamil
		String subject = "JKN E-SHOP Email Verification Code";
		String message = "Welcome to JKN E-SHOP \n verify your account using this link ";
		String frontend_url = "http://localhost:3000/verify-seller/";
		emailService.sendVerificationOtpEmail(seller.getEmail(),verificationCode.getOtp(),subject,message + frontend_url);
   	return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
   	
       }
    
    @GetMapping("/{id}/get")
    public ResponseEntity<Seller>getSellerById (
	    @PathVariable Long id)throws SellerException{
	Seller seller = sellerService.getSellerById(id);
	
   	return new ResponseEntity<>(seller, HttpStatus.OK);
   	
       }
    
    @GetMapping("/profile/get")
    public ResponseEntity<Seller>getSellerProfileByJwt(
	    @RequestHeader("Authorization") String jwt)throws Exception{
 Seller seller = sellerService.getSellerProfile(jwt);
   	return new ResponseEntity<>(seller, HttpStatus.OK);
   	
       }
    
//    @GetMapping("/report/get")
//    public ResponseEntity<SellerReport>getSellerReport(
//	    @RequestHeader("Authorization") String jwt)throws Exception{
// Seller seller = sellerService.getSellerProfile(jwt);
//   	return new ResponseEntity<>(seller, HttpStatus.OK);
//   	
//       }
    
    @GetMapping("/all/get")
    public ResponseEntity<List<Seller>>getAllSellers(
	    @RequestParam(required = false) AccountStatus status)throws Exception{
	List<Seller> sellers = sellerService.getAllSellers(status);
	
   	return ResponseEntity.ok(sellers);
       }
    
    @PatchMapping("/update/seller")
    public ResponseEntity<Seller>updateSeller(
	    @RequestHeader("Authorization") String jwt,
	    @RequestBody Seller seller)throws Exception{
 Seller profile = sellerService.getSellerProfile(jwt);
 Seller updatedSelller = sellerService.updateSeller(seller, profile.getId());
   	return ResponseEntity.ok(updatedSelller);
       }
    
    @DeleteMapping("/delete/{id}/seller")
    public ResponseEntity<Seller>deleteSeller(
	    @PathVariable Long id)throws Exception{
 sellerService.deleteSeller(id);
   	return ResponseEntity.noContent().build();
       }
}
