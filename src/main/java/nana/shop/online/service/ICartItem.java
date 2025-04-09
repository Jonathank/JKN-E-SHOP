/**
 * 
 */
package nana.shop.online.service;

import nana.shop.online.model.CartItems;

/**
 * @author JONATHAN
 */
public interface ICartItem {
   
    CartItems updateCartItems(Long userId, Long cartItemId,CartItems cartItems);
    void removeCartItems(Long userId, Long cartItemId);
    CartItems findByCartItemById(Long cartItemId);

}
