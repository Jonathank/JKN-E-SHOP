/**
 * 
 */
package nana.shop.online.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nana.shop.online.config.JwtProvider;
import nana.shop.online.exception.UserException;
import nana.shop.online.model.User;
import nana.shop.online.repositories.UserRepository;
import nana.shop.online.service.IUserService;

/**
 * @author JONATHAN
 */
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    
    @Override
    public User findUserByEmail(String email) throws UserException {
	User user = userRepository.findByEmail(email);
	if (user == null) {
	    throw new UserException("User not found with email: " + email);
	}
	return user;
    }

   
    @Override
    public User findUserByJwtToken(String jwtToken) throws UserException {
	String email = jwtProvider.getEmailFromToken(jwtToken);
	User user = this.findUserByEmail(email);
	if(user == null) {
               throw new UserException("User not found with email: " + email);
    }

	    return user;
}
    
    
}