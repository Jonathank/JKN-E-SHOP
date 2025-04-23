/**
 * 
 */
package nana.shop.online.service.impl;

import java.io.ObjectInputFilter.Status;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;
import nana.shop.online.config.PaymentConfig;
import nana.shop.online.domain.PaymentOrderStatus;
import nana.shop.online.domain.PaymentStatus;
import nana.shop.online.exception.PaymentOrderException;
import nana.shop.online.model.Order;
import nana.shop.online.model.PaymentOrder;
import nana.shop.online.model.User;
import nana.shop.online.repositories.OrderRepository;
import nana.shop.online.repositories.PaymentOrderRepository;
import nana.shop.online.service.IPaymentService;

/**
 * @author JONATHAN
 */
@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService{
    
    private final OrderRepository orderRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final PaymentConfig paymentConfig;
    
    @Override
    public PaymentOrder createOrder(User user, Set<Order> orders) {
	Double amount = orders.stream()
		.mapToDouble(Order::getTotalSellingPrice).sum();
	
	PaymentOrder paymentOrder = new PaymentOrder();
	paymentOrder.setAmount(amount);
	paymentOrder.setUser(user);
	paymentOrder.setOrders(orders);
	
	return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderByOrderId(Long orderId) throws PaymentOrderException {
	
	return paymentOrderRepository.findById(orderId)
		.orElseThrow(() -> new PaymentOrderException("payment order not found!"));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentId) throws PaymentOrderException {
	PaymentOrder paymentOrder = paymentOrderRepository.findByPaymentLinkId(paymentId);
	if(paymentOrder == null) {
	    throw new PaymentOrderException("Payment order not found with provided payment link Id: "+paymentId);
	}
	return paymentOrder;
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {
	if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
	    RazorpayClient razorpayClient = new RazorpayClient(paymentConfig.RAZORPAY_KEY_ID,paymentConfig.RAZORPAY_SECRET);
	    
	    Payment payment = razorpayClient.payments.fetch(paymentId);
	    String status = payment.get("status");
	    if(status.equals("captured")) {
		Set<Order> orders = paymentOrder.getOrders();
		for(Order order : orders) {
		    order.setPaymentStatus(PaymentStatus.COMPLETED);
		    orderRepository.save(order);
		}
		paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
		paymentOrderRepository.save(paymentOrder);
		return true;
	    }
	    paymentOrder.setStatus(PaymentOrderStatus.FAILED);
	    paymentOrderRepository.save(paymentOrder);
	    return false;
	}
	return false;
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(User user, Double amount, Long orderId) throws RazorpayException {
	amount =  amount*100;
	try {
	    RazorpayClient razorpayClient = new RazorpayClient(paymentConfig.RAZORPAY_KEY_ID,paymentConfig.RAZORPAY_SECRET);
	    JSONObject paymentLinkRequest = new JSONObject();
	    paymentLinkRequest.put("amount", amount);
	    paymentLinkRequest.put("Currency", "INR");
	    
	    JSONObject customer = new JSONObject();
	    customer.put("name", user.getFullName());
	    customer.put("email", user.getEmail());
	    
	    paymentLinkRequest.put("customer", customer);
	    
	    //notify user
	    JSONObject notify = new JSONObject();
	    notify.put("email", true);
	    paymentLinkRequest.put("notify", notify);
	    
	    paymentLinkRequest.put("callback_url", 
		    "http://localhost:3000/payment-success/"+orderId);
	    paymentLinkRequest.put("callback_method", "get");
	    
	    PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);
	    
	    String paymentLinkUrl = paymentLink.get("short_url");
	    String paymentLinkId = paymentLink.get("id");
	    
	    return paymentLink;
	} catch (Exception e) {
	    throw new RazorpayException(e.getMessage());
	}
    }

    @Override
    public String createStripePaymentLink(User user, Double amount, Long orderId) throws StripeException {

	Stripe.apiKey = paymentConfig.STRIPE_SECRET_KEY;
	
	SessionCreateParams params = SessionCreateParams.builder()
		.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
		.setMode(SessionCreateParams.Mode.PAYMENT)
		.setSuccessUrl("http://localhost:3000/payment-success/"+orderId)
		.setCancelUrl("http://localhost:3000/payment-cancel/")
		.addLineItem(SessionCreateParams.LineItem.builder()
			.setQuantity(1L)
			.setPriceData(SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency("usd")
				.setUnitAmount((long) (amount*100))
				.setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
					.setName("Kyeyune Jonathan Payment")
					.build()
					).build()
				).build()
			).build();
	Session session = Session.create(params);
	
	return session.getReturnUrl();
    }

}
