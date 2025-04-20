/**
 * 
 */
package nana.shop.online.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nana.shop.online.domain.OrderStatus;
import nana.shop.online.exception.OrderException;
import nana.shop.online.model.Order;
import nana.shop.online.model.Seller;
import nana.shop.online.service.impl.OrderService;
import nana.shop.online.service.impl.SellerService;

/**
 * @author JONATHAN
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/seller/orders")
public class SellerOrderController {

    private final OrderService orderService;
    private final SellerService sellerService;
    
    @GetMapping("get/all")
    public ResponseEntity<List<Order>> getAllOrdersHandler(
	    @RequestHeader("Authorization")String jwt
	    ) throws Exception{
	Seller seller = sellerService.getSellerProfile(jwt);
	List<Order>orders = orderService.sellersOrders(seller.getId());
	return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }
    
    @PatchMapping("/updat/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrdersHandler(
	    @RequestHeader("Authorization")String jwt,
	    @PathVariable Long orderId,
	    @PathVariable OrderStatus orderStatus
	    ) throws OrderException{
	
	Order orders = orderService.updateOrderStatus(orderId, orderStatus);
	
	return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }
}
