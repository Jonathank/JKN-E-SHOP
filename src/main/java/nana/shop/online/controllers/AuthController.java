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
import nana.shop.online.domain.USER_ROLE;
import nana.shop.online.model.VerificationCode;
import nana.shop.online.request.LoginRequest;
import nana.shop.online.request.SignUpRequest;
import nana.shop.online.response.ApiResponse;
import nana.shop.online.response.AuthResponse;
import nana.shop.online.service.impl.AuthService;

/**
 * @author JONATHAN
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {

  //  private final UserRepository userRepository;
    private final AuthService authService;
    
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse>createUserHandler(@RequestBody SignUpRequest signUpRequest) throws Exception{
	String jwt = authService.createUser(signUpRequest);
	AuthResponse authResponse = new AuthResponse();
	authResponse.setToken(jwt);
	authResponse.setMessage("User created successfully");
	authResponse.setUserRole(USER_ROLE.ROLE_CUSTOMER);
	
	return ResponseEntity.ok(authResponse);
    }
    
    @PostMapping("/send/signup-Otp")
    public ResponseEntity<ApiResponse>sendOtpHandler(@RequestBody VerificationCode Request) throws Exception{
	authService.sendLoginOtp(Request.getEmail());
	ApiResponse response = new ApiResponse();
	response.setMessage("Opt sent successfully");
	return ResponseEntity.ok(response);
    }
    
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse>signinHandler(@RequestBody LoginRequest request) throws Exception{
	AuthResponse authResponse = authService.signin(request);
	return ResponseEntity.ok(authResponse);
    }
    

}
