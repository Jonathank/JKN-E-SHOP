/**
 * 
 */
package nana.shop.online.response;

import lombok.Data;
import nana.shop.online.domain.USER_ROLE;

/**
 * @author JONATHAN
 */
@Data
public class AuthResponse {

    private String token;//jwt token
    private String message;
    private USER_ROLE userRole;
    
}
