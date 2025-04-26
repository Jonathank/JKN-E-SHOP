/**
 * 
 */
package nana.shop.online.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;
import nana.shop.online.exception.CouponException;
import nana.shop.online.exception.UserException;
import nana.shop.online.model.Cart;
import nana.shop.online.model.Coupon;
import nana.shop.online.model.User;
import nana.shop.online.service.impl.CartService;
import nana.shop.online.service.impl.CouponService;
import nana.shop.online.service.impl.UserService;

/**
 * @author JONATHAN
 */
@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
public class CouponController {
    
    private final CouponService couponService;
    private final UserService userService;
    private final CartService cartService;
    
    @PostMapping("/appy/coupon")
    public ResponseEntity<Cart>applyCoupon(
	    @RequestParam String apply,
	    @RequestParam String code,
	    @RequestParam double orderValue,
	    @RequestHeader("Authorization") String jwt
	    ) throws UserException, CouponException{
	
	User user = userService.findUserByJwtToken(jwt);
	Cart cart;
	
	if(apply.equals("true")) {
	    cart = couponService.applyCoupon(code, orderValue, user);
	}
	else {
	    cart = couponService.removeCoupon(code, user);
	}
	
	return new ResponseEntity<>(cart, HttpStatus.OK);
    }
    
    @PostMapping("/admin/create-coupon")
    public ResponseEntity<Coupon>createCoupon(
	    @RequestBody Coupon coupon
	    ){
	Coupon createdCoupon = couponService.createCoupon(coupon);
	return new ResponseEntity<>(createdCoupon, HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete-coupon/{couponId}")
    public ResponseEntity<?>deleteCoupon(
	    @PathVariable Long couponId
	    ) throws CouponException{
	 couponService.deleteCoupon(couponId);
	return ResponseEntity.ok("Coupon deleted successfully");
    }

    @GetMapping("/admin/all/coupons")
    public ResponseEntity<List<Coupon>>deleteCoupon() throws CouponException{
	 List<Coupon> coupons = couponService.findAllCoupons();
	return ResponseEntity.ok(coupons);
    }

}
