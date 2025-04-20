/**
 * 
 */
package nana.shop.online.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

import lombok.RequiredArgsConstructor;
import nana.shop.online.domain.PaymentMethod;
import nana.shop.online.exception.OrderException;
import nana.shop.online.exception.SellerException;
import nana.shop.online.exception.UserException;
import nana.shop.online.model.Address;
import nana.shop.online.model.Cart;
import nana.shop.online.model.Order;
import nana.shop.online.model.OrderItems;
import nana.shop.online.model.PaymentOrder;
import nana.shop.online.model.Seller;
import nana.shop.online.model.SellerReport;
import nana.shop.online.model.User;
import nana.shop.online.repositories.PaymentOrderRepository;
import nana.shop.online.response.PaymentLinkResponse;
import nana.shop.online.service.impl.CartService;
import nana.shop.online.service.impl.OrderService;
import nana.shop.online.service.impl.PaymentService;
import nana.shop.online.service.impl.SellerReportService;
import nana.shop.online.service.impl.SellerService;
import nana.shop.online.service.impl.UserService;

/**
 * @author JONATHAN
 */
@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final UserService userService;
    private final CartService cartService;
    private final OrderService orderService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final PaymentService paymentService;
    private final PaymentOrderRepository paymentOrderRepository;
    
    
    @PostMapping("/make/order")
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(
	    @RequestHeader("Authorization") String jwt,
	    @RequestParam PaymentMethod paymentMethod,
	    @RequestBody Address shippingAddress
	    ) throws OrderException, UserException, RazorpayException, StripeException{
	
	User user = userService.findUserByJwtToken(jwt);
	Cart cart = cartService.findUserCart(user);
	Set<Order> orders = orderService.createOrder(user,shippingAddress, cart);
	
	PaymentOrder paymentOrder = paymentService.createOrder(user, orders);
	
	PaymentLinkResponse response = new PaymentLinkResponse();
	
	if(paymentMethod.equals(PaymentMethod.RAZORPAY)) {
	    PaymentLink payment = paymentService.createRazorpayPaymentLink(
		    user, 
		    paymentOrder.getAmount(), 
		    paymentOrder.getId());
	    
	    String paymentUrl = payment.get("short_url");
	    String paymentUrlId = payment.get("id");
	    
	    response.setPayment_link_url(paymentUrl);
	    paymentOrder.setPaymentLinkId(paymentUrlId);
	    
	    paymentOrderRepository.save(paymentOrder);
	    
	}
	else {
	    String paymentUrl = paymentService.createStripePaymentLink(
		    user, 
		    paymentOrder.getAmount(), 
		    paymentOrder.getId());
	    response.setPayment_link_url(paymentUrl);
	}
	
	return new ResponseEntity<>(response,HttpStatus.OK);
    }
    
    @GetMapping("/user-history/get")
    public ResponseEntity<List<Order>> UserOrderHistoryHandler(
	    @RequestHeader("Authorization") String jwt
	    ) throws OrderException, UserException{
	User user = userService.findUserByJwtToken(jwt);
	List<Order>orders = orderService.usersOrderHistory(user.getId());
	
	return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }
    
    @GetMapping("/get/order/{orderId}")
    public ResponseEntity<Order> getOrderById(
	    @PathVariable Long orderId,
	    @RequestHeader("Authorization")String jwt) throws OrderException,UserException{
	User user = userService.findUserByJwtToken(jwt);
	Order order = orderService.findOrderById(orderId);
	return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }
    
    
    @GetMapping("/get/item/{orderItemId}")
    public ResponseEntity<OrderItems> getOrderItemById(
	    @PathVariable Long orderItemId,
	    @RequestHeader("Authorization")String jwt) throws OrderException,UserException{
    
	User user = userService.findUserByJwtToken(jwt);
	OrderItems orderItems = orderService.getOrderItemById(orderItemId);
	return new ResponseEntity<>(orderItems, HttpStatus.ACCEPTED);
    }
    
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancleOrder(
	    @PathVariable Long orderId,
	    @RequestHeader("Authorization")String jwt) throws OrderException,UserException,SellerException{
	    
	User user = userService.findUserByJwtToken(jwt);
	Order order = orderService.cancelOrder(orderId, user);
	
	Seller seller = sellerService.getSellerById(order.getSellerId());
	SellerReport report = sellerReportService.getSellerReport(seller);
	
	report.setCanceledOrders(report.getCanceledOrders()+1);
	report.setTotalRefunds(report.getTotalRefunds()+order.getTotalSellingPrice());
	sellerReportService.updateSellerReport(report);
	
	return new ResponseEntity<>(order, HttpStatus.OK); 
    }
}
