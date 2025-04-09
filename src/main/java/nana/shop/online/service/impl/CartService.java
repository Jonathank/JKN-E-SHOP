/**
 * 
 */
package nana.shop.online.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nana.shop.online.model.Cart;
import nana.shop.online.model.CartItems;
import nana.shop.online.model.Product;
import nana.shop.online.model.User;
import nana.shop.online.repositories.CartItemRepository;
import nana.shop.online.repositories.CartRepository;
import nana.shop.online.service.ICart;

/**
 * @author JONATHAN
 */
@Service
@RequiredArgsConstructor
public class CartService implements ICart {
   
     private final CartItemRepository cartItemRepository;
   
     private final CartRepository cartRepository;

    @Override
    public CartItems addCartItem(User user, Product product, String size, int quantity) {
	Cart cart = findUserCart(user);
	
	CartItems cartItemPresent = cartItemRepository.findByCartAndProductAndSize(cart,product,size);
	
	if (cartItemPresent == null) {
	    CartItems cartItem = new CartItems();
	    cartItem.setUserId(user.getId());
	    cartItem.setProduct(product);
	    cartItem.setSize(size);
	    cartItem.setQuantity(quantity);
	    
	    int totalPrice = quantity * product.getSellingPrice();
	    cartItem.setSellingPrice(totalPrice);
	    cartItem.setMrpPrice(quantity * product.getMrpPrice());
	    
	    cart.getCartItems().add(cartItem);
	    cartItem.setCart(cart);

	    //cartRepository.save(cart);
	    return cartItemRepository.save(cartItem);
	}
	
	return cartItemPresent;
    
    }

    @Override
    public Cart findUserCart(User user) {
    Cart cart = cartRepository.findUserById(user.getId());
    
    int totalPrice = 0;
    int totalDiscountedPrice = 0;
    int totalItems=0;
    
    for (CartItems cartItem : cart.getCartItems()) {
	totalPrice += cartItem.getMrpPrice();
	totalDiscountedPrice += cartItem.getSellingPrice();
	totalItems += cartItem.getQuantity();
    }
    cart.setMrpPrice(totalPrice);
    cart.setTotalSellingPrice(totalDiscountedPrice);
    cart.setTotalItems(totalItems);
    cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountedPrice));
    
    return cart;
    }

    /**
     * @param mrpPrice
     * @param sellingPrice
     * @return
     */
    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
	if(mrpPrice <=0) {
	   return 0;
	}
	
	double discount = mrpPrice - sellingPrice;
	double discountPercentage = (discount/mrpPrice)*100;
	return (int) discountPercentage;
    }
}
