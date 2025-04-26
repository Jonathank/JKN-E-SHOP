/**
 * 
 */
package nana.shop.online.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.codec.CodecPolicy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nana.shop.online.exception.CouponException;
import nana.shop.online.model.Cart;
import nana.shop.online.model.Coupon;
import nana.shop.online.model.User;
import nana.shop.online.repositories.CartRepository;
import nana.shop.online.repositories.CouponRepository;
import nana.shop.online.repositories.UserRepository;
import nana.shop.online.service.ICouponService;

/**
 * @author JONATHAN
 */
@Service
@RequiredArgsConstructor

public class CouponService implements ICouponService{
    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    
    @Override
    public Cart applyCoupon(String code, double orderValue, User user) throws CouponException {
	Coupon coupon = couponRepository.findBycode(code);
	Cart cart = cartRepository.findUserById(user.getId());
	
	if(coupon == null) {
	    throw new CouponException("Coupon not valid");
	}
	if(user.getUsedCoupons().contains(coupon)) {
	    throw new CouponException("Coupon already used");
	}
	if(orderValue < coupon.getMinimumOrderValue()){
	    throw new CouponException("Valid for minimum order value "+ coupon.getMinimumOrderValue());
	}
	
	if(coupon.isActive() &&
	    LocalDate.now().isAfter(coupon.getValidityStartDate())
	    && LocalDate.now().isBefore(coupon.getValidityEndDate())) {
	    user.getUsedCoupons().add(coupon);
	    userRepository.save(user);
	    
	    double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage())/100;
	    cart.setTotalSellingPrice(cart.getTotalSellingPrice()- discountedPrice);
	    cartRepository.save(cart);
	    return cart;
	}
	 throw new CouponException("Coupon not valid");
    }

    @Override
    public Cart removeCoupon(String code, User user) throws CouponException {
	Coupon coupon = couponRepository.findBycode(code);
	
	if(coupon == null) {
	    throw new CouponException("Coupon not found..."); 
	}
	Cart cart = cartRepository.findUserById(user.getId());
	double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage())/100;
	cart.setTotalSellingPrice(cart.getTotalSellingPrice()+ discountedPrice);
	cart.setCouponCode(null);
	    
	return cartRepository.save(cart);
    }

    @Override
    public Coupon findCouponById(Long id) throws CouponException {
	
	return couponRepository.findById(id)
		.orElseThrow(() -> new CouponException("Coupon not found!!"));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
	
	return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupons() {
	
	return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCoupon(Long id) throws CouponException {
	findCouponById(id);
	couponRepository.deleteById(id);
    }

}
