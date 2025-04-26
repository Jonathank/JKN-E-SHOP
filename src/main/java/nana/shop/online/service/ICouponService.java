/**
 * 
 */
package nana.shop.online.service;

import java.util.List;

import nana.shop.online.exception.CouponException;
import nana.shop.online.model.Cart;
import nana.shop.online.model.Coupon;
import nana.shop.online.model.User;

/**
 *@author JONATHAN 
 */
public interface ICouponService {

    Cart applyCoupon(String code, double orderValue,User user) throws CouponException;
    Cart removeCoupon(String code,User user) throws CouponException;
    Coupon findCouponById(Long id) throws CouponException;
    Coupon createCoupon(Coupon coupon);
    List<Coupon>findAllCoupons();
    void deleteCoupon(Long id) throws CouponException;
}
