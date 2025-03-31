/**
 * 
 */
package nana.shop.online.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nana.shop.online.model.User;
import nana.shop.online.service.IUserService;

/**
 * @author JONATHAN
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user")
public class UserController {

     private final IUserService userService;
    
     @GetMapping("/profile")
     public ResponseEntity<User> getProfileHandler(@RequestHeader("Authorization")
     String token) throws Exception {
     User user = userService.findUserByJwtToken(token);
     return ResponseEntity.ok(user);
     }

//     @PutMapping("/update")
//     public ResponseEntity<User>
//     updateProfileHandler(@RequestHeader("Authorization") String token,
//     @RequestBody User user) throws Exception {
//     User existingUser = userService.findUserByJwtToken(token);
//     existingUser.setFirstName(user.getFirstName());
//     existingUser.setLastName(user.getLastName());
//     existingUser.setPhoneNumber(user.getPhoneNumber());
//     userRepository.save(existingUser);
//     return ResponseEntity.ok(existingUser);
//     }

    // @PutMapping("/change-password")
    // public ResponseEntity<ApiResponse>
    // changePasswordHandler(@RequestHeader("Authorization") String token,
    // @RequestBody ChangePasswordRequest request) throws Exception {
    // User user = userService.findUserByJwtToken(token);
    // if (!user.getPassword().equals(request.getOldPassword())) {
    // throw new Exception("Old password is incorrect");
    // }
    // user.setPassword(request.getNewPassword());
    // userRepository.save(user);
    // ApiResponse response = new ApiResponse();
    // response.setMessage("Password changed successfully");
    // return ResponseEntity.ok(response);
    // }

    // @PostMapping("/forgot-password")
    // public ResponseEntity<ApiResponse> forgotPasswordHandler(@RequestBody
    // ForgotPasswordRequest request)
    // throws Exception {
    // User user = userService.findUserByEmail(request.getEmail());
    // if (user == null) {
    // throw new Exception("User not found with email: " + request.getEmail());
    // }
    // String token = jwtProvider.generateToken(new
    // UsernamePasswordAuthenticationToken(user.getEmail(), null,
    // Collections.singleton(new SimpleGrantedAuthority("CHANGE_PASSWORD"))));
    // String message = "Click on the link to reset your password: " +
    // "http://localhost:8080/user/reset-password?token="
    // + token;
    // // send email to user.getEmail() with message
    // ApiResponse response = new ApiResponse();
    // response.setMessage("Password reset link sent to your email");	
}
