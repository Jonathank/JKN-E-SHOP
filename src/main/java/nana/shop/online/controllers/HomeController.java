/**
 * 
 */
package nana.shop.online.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JONATHAN
 */
@RestController
public class HomeController {

    @GetMapping()
    public String HomeControllerHandler() {
	return "Welcome to JKN E SHOP";
    }
    
}
