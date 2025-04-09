/**
 * 
 */
package nana.shop.online.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nana.shop.online.model.CartItems;
import nana.shop.online.model.User;
import nana.shop.online.repositories.CartItemRepository;
import nana.shop.online.service.ICartItem;

/**
 * @author JONATHAN
 */
@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItem {

     private final CartItemRepository cartItemRepository;
     
    @Override
    public CartItems updateCartItems(Long userId, Long cartItemId, CartItems cartItems) {
	// Find the cart item by ID
	CartItems cartItemPresent = findByCartItemById(cartItemId);
	
	User cartItemUser = cartItemPresent.getCart().getUser();
	
	if (cartItemUser.getId().equals(userId)) {
	    cartItemPresent.setQuantity(cartItems.getQuantity());
	    cartItemPresent.setSize(cartItems.getSize());
	    cartItemPresent.setMrpPrice(cartItemPresent.getQuantity() * cartItemPresent.getProduct().getMrpPrice());
	    cartItemPresent.setSellingPrice(cartItemPresent.getQuantity() * cartItemPresent.getProduct().getSellingPrice());
	    cartItemPresent.setProduct(cartItems.getProduct());

	    return cartItemRepository.save(cartItemPresent);
	} else {
	    throw new RuntimeException("You are not authorized to update this item");
	}
    }

    @Override
    public void removeCartItems(Long userId, Long cartItemId) {
	CartItems cartItemPresent = findByCartItemById(cartItemId);
	
	User cartItemUser = cartItemPresent.getCart().getUser();
	if (cartItemUser.getId().equals(userId)) {
        cartItemRepository.delete(cartItemPresent);
            } else {
       throw new RuntimeException("You are not authorized to delete this item");
            }
    }

    @Override
    public CartItems findByCartItemById(Long cartItemId) {
	return cartItemRepository.findById(cartItemId)
	.orElseThrow(() -> new RuntimeException("Cart item not found"));		
    }
      
}
