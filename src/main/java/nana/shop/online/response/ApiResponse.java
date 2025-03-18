/**
 * 
 */
package nana.shop.online.response;


import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * @author JONATHAN
 */
@Data
@AllArgsConstructor
public class ApiResponse {

    private String message;
    private Object data;
    
}
