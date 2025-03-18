/**
 * 
 */
package nana.shop.online.model;

import lombok.Data;
import nana.shop.online.domain.PaymentStatus;

/**
 * @author JONATHAN
 */
@Data
public class PaymentDetails {

    private String paymentId;
    private String razorpayPaymentLinkId;
    private String razorpayPaymentLinkReferenceId;
    private String razorpayPaymentLinkStatus;
    private String razorpayPaymentIdZMSP;
    private PaymentStatus status;
}
