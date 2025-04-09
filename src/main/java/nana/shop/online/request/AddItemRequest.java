/**
 * 
 */
package nana.shop.online.request;

import lombok.Data;

/**
 * @author JONATHAN
 */
@Data
public class AddItemRequest {

    private Long productId;
    private String size;
    private int quantity;

}
