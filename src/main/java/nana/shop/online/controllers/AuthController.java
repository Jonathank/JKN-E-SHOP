/**
 * 
 */
package nana.shop.online.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nana.shop.online.model.User;

/**
 * @author JONATHAN
 */
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {

    public ResponseEntity<User>createUserHandler(){
	return null;
    }
}
