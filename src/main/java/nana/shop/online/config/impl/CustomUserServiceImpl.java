/**
 * 
 */
package nana.shop.online.config.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nana.shop.online.domain.USER_ROLE;
import nana.shop.online.model.Seller;
import nana.shop.online.model.User;
import nana.shop.online.repositories.SellerRepository;
import nana.shop.online.repositories.UserRepository;

/**
 *@author JONATHAN 
 */
@RequiredArgsConstructor
@Service
public class CustomUserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    private static final String SELLER_PREFIX = "seller_";
   // private static final String CUSTOMER_PREFIX = "customer_";
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	if (username.startsWith(SELLER_PREFIX)) {
	    String actualUsername = username.substring(SELLER_PREFIX.length());
	    Seller seller = sellerRepository.findByEmail(actualUsername);
	    
	      if (seller != null) {
		  return buildUserDetails(seller.getEmail(), seller.getPassword(), seller.getRole());
	      }else {
		  throw new UsernameNotFoundException("Seller not found with email: " + username);
	      }
	    
	}
	else {
	User user = userRepository.findByEmail(username);
	if (user != null) {
	    return buildUserDetails(user.getEmail(), user.getPassword(), user.getRole());
	    
	}else {
	    throw new UsernameNotFoundException("User not found with email: " + username);
	}
	}
    }

    
    /**
     * @param email
     * @param password
     * @param role
     * @return
     */
    private UserDetails buildUserDetails(String email, String password, USER_ROLE role) {
	if (role == null) role = USER_ROLE.ROLE_CUSTOMER;
	
	List<GrantedAuthority> authorities = new ArrayList<>();
	authorities.add(new SimpleGrantedAuthority(role.toString()));
	
	return new org.springframework.security.core.userdetails.User(
		email, password, authorities);
    }

}
