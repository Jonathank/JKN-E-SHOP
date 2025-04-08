/**
 * 
 */
package nana.shop.online.service;

import nana.shop.online.request.LoginRequest;
import nana.shop.online.request.SignUpRequest;
import nana.shop.online.response.AuthResponse;

/**
 * @author JONATHAN
 */
public interface IAuthService {

    String createUser(SignUpRequest signUpRequest) throws Exception;

    void sendLoginOtp(String userEmail) throws Exception;

    /**
     * @param loginRequest
     * @return
     * @throws Exception 
     */
    AuthResponse signin(LoginRequest loginRequest) throws Exception;
}
