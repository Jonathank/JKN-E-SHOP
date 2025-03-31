/**
 * 
 */
package nana.shop.online.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nana.shop.online.config.JwtProvider;
import nana.shop.online.config.impl.CustomUserServiceImpl;
import nana.shop.online.domain.USER_ROLE;
import nana.shop.online.model.Cart;
import nana.shop.online.model.Seller;
import nana.shop.online.model.User;
import nana.shop.online.model.VerificationCode;
import nana.shop.online.repositories.CartRepository;
import nana.shop.online.repositories.UserRepository;
import nana.shop.online.repositories.VerificationCodeRepository;
import nana.shop.online.request.LoginRequest;
import nana.shop.online.request.SignUpRequest;
import nana.shop.online.response.AuthResponse;
import nana.shop.online.service.IAuthService;
import nana.shop.online.utils.OtpUtils;

/**
 * @author JONATHAN
 */
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final CustomUserServiceImpl customUserServiceImpl;
    private final EmailService emailService;
    private final JwtProvider JwtProvider;
    
    @Override
    public String createUser(SignUpRequest signUpRequest) throws Exception {
	//String SIGNING_PREFIX = "signing_";
	
	VerificationCode verificationCode = verificationCodeRepository
		.findByEmail(signUpRequest.getEmail());
	
	if (verificationCode == null || !verificationCode.getOtp().equals(signUpRequest.getOtp())) {
	    throw new Exception("wrong otp...");
	}
	
	User user = userRepository.findByEmail(signUpRequest.getEmail());
	
	if (user == null) {
	    User newUser = new User();
	    newUser.setEmail(signUpRequest.getEmail());
	    newUser.setFullName(signUpRequest.getFullName());
	    newUser.setPassword(passwordEncoder.encode( signUpRequest.getOtp()));
	    newUser.setRole(USER_ROLE.ROLE_CUSTOMER);
	    newUser.setMobile("+256706831793");
	    
	   user = userRepository.save(newUser);
	   
	   Cart cart = new Cart();
	   cart.setUser(user);
	   cartRepository.save(cart);
	}
	
	List<GrantedAuthority> authorities = new ArrayList<>();
	authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));
	
	Authentication authentication = new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), null, authorities);
	SecurityContextHolder.getContext().setAuthentication(authentication);
	
	return JwtProvider.generateToken(authentication);
    }

    @Override
    public void sendLoginOtp(String userEmail) throws Exception {
	String SIGNING_PREFIX = "signin_";
	
	if(userEmail.startsWith(SIGNING_PREFIX)) {
        userEmail = userEmail.substring(SIGNING_PREFIX.length());
        
        User user = userRepository.findByEmail(userEmail);
	if (user == null) {
	    throw new Exception("User not found");
	}
	}
	VerificationCode isExist = verificationCodeRepository.findByEmail(userEmail);
	if (isExist != null) {
        verificationCodeRepository.delete(isExist);
    }
	String otp = OtpUtils.generateOtp();
	VerificationCode verificationCode = new VerificationCode();
	verificationCode.setEmail(userEmail);
	verificationCode.setOtp(otp);
	
	verificationCodeRepository.save(verificationCode);
	
	 //send otp to user eamil
	String subject = "JKN E-SHOP Login OTP";
	String message = "Welcome to JKN E-SHOP \nYour Login OTP is " + otp;
	emailService.sendVerificationOtpEmail(userEmail,otp,subject,message);
	    
	}
    
    @Override
    public AuthResponse signin(LoginRequest loginRequest) throws Exception {
	String username = loginRequest.getEmail();
	String otp = loginRequest.getOtp();
	
	Authentication authentication = authenticate(username, otp);
	SecurityContextHolder.getContext().setAuthentication(authentication);
	
	String token = JwtProvider.generateToken(authentication);
	AuthResponse authResponse = new AuthResponse();
	authResponse.setToken(token);
	authResponse.setMessage("Login success");
	
	Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
	authResponse.setUserRole(USER_ROLE.valueOf(role));
	
	return authResponse;
	
    }

    /**
     * @param username
     * @param otp
     * @return
     * @throws Exception 
     */
    private Authentication authenticate(String username, String otp) throws Exception {
	UserDetails userDetails = customUserServiceImpl.loadUserByUsername(username);
	String SELLER_PREFIX = "seller_";
	
	if (username.startsWith(SELLER_PREFIX)) {
	    username = username.substring(SELLER_PREFIX.length());
	}
	if (userDetails == null) {
	    throw new Exception("Invalid username");
	}
	
	VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
	if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
	    throw new Exception("Invalid otp");
	}
	return new UsernamePasswordAuthenticationToken(
		userDetails, null, userDetails.getAuthorities());
    }
    }
    


