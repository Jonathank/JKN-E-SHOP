/**
 * 
 */
package nana.shop.online.response;

import lombok.Data;

/**
 * @author JONATHAN
 */
@Data
public class PaymentLinkResponse {

    private String payment_link_url;
    private String payment_link_id;
}
