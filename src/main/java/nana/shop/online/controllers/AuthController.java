/**
 * 
 */
package nana.shop.online.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nana.shop.online.model.User;
import nana.shop.online.repositories.UserRepository;
import nana.shop.online.request.SignUpRequest;

/**
 * @author JONATHAN
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {

    private final UserRepository userRepository;
    
    @PostMapping("/signup")
    public ResponseEntity<User>createUserHandler(@RequestBody SignUpRequest signUpRequest){
	User user = new User();
	user.setEmail(signUpRequest.getEmail());
	user.setFullName(signUpRequest.getFullName());
	
	User savedUser = userRepository.save(user);
	return ResponseEntity.ok(savedUser);
    }

    public ResponseEntity<User> loginUserHandler(@RequestBody SignUpRequest signUpRequest) {
	return null;
    }
}
