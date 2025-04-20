/**
 * 
 */
package nana.shop.online.service;

import java.util.Set;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

import nana.shop.online.exception.PaymentOrderException;
import nana.shop.online.model.Order;
import nana.shop.online.model.PaymentOrder;
import nana.shop.online.model.User;

/**
 * @author JONATHAN
 */
public interface IPaymentService {

    PaymentOrder createOrder(User user, Set<Order> orders);
    PaymentOrder getPaymentOrderByOrderId(Long orderId) throws PaymentOrderException;
    PaymentOrder getPaymentOrderByPaymentId(String paymentId) throws PaymentOrderException;
    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId,
	    String paymentLinkId) throws RazorpayException;
    PaymentLink createRazorpayPaymentLink(User user,
	   Double amount,
	    Long orderId) throws RazorpayException;
    String createStripePaymentLink(User user,
	    Double amount, Long orderIdf) throws StripeException;
   
    
}
