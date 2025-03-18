/**
 * 
 */
package nana.shop.online.request;

import lombok.Data;

/**
 * @author JONATHAN
 */
@Data
public class SignUpRequest {

    private String email;
    private String fullName;
    private String otp;
}
