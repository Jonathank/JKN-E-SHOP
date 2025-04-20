/**
 * 
 */
package nana.shop.online.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nana.shop.online.domain.PaymentOrderStatus;
import nana.shop.online.model.Order;
import nana.shop.online.model.PaymentOrder;
import nana.shop.online.model.Seller;
import nana.shop.online.model.SellerReport;
import nana.shop.online.model.User;
import nana.shop.online.response.ApiResponse;
import nana.shop.online.response.PaymentLinkResponse;
import nana.shop.online.service.impl.PaymentService;
import nana.shop.online.service.impl.SellerReportService;
import nana.shop.online.service.impl.SellerService;
import nana.shop.online.service.impl.UserService;

/**
 * @author JONATHAN
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    
    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse>paymentSuccessHandler(
	    @PathVariable String paymentId,
	    @RequestParam String paymentLinkId,
	    @RequestHeader("Authorization") String jwt
	    ) throws Exception{
	User user = userService.findUserByJwtToken(jwt);
	
	PaymentLinkResponse paymentLinkResponse;
	PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentId);
	
	boolean paymentSuccess = paymentService.proceedPaymentOrder(
		paymentOrder, paymentId, paymentLinkId);
	if(paymentSuccess) {
	    for(Order order : paymentOrder.getOrders()) {
	// transactionService.createTransaction(order)
		Seller seller = sellerService.getSellerById(order.getSellerId());
		SellerReport sellerReport = sellerReportService.getSellerReport(seller);
		sellerReport.setTotalOrders(sellerReport.getTotalOrders()+1);
		sellerReport.setTotalEarnings(sellerReport.getTotalEarnings() + order.getTotalSellingPrice());
		sellerReport.setTotalSales(sellerReport.getTotalSales()+ order.getOrderItems().size());
		sellerReportService.updateSellerReport(sellerReport);
	    }
	}
	ApiResponse response = new ApiResponse();
	response.setMessage("Payment Successful");
	return new 
		ResponseEntity<>(response, HttpStatus.ACCEPTED);
	
    }
}
