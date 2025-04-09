/**
 * 
 */
package nana.shop.online.service;

import nana.shop.online.model.Cart;
import nana.shop.online.model.CartItems;
import nana.shop.online.model.Product;
import nana.shop.online.model.User;

/**
 * @author JONATHAN
 */
public interface ICart {
    
    CartItems addCartItem(
	    User user,
	    Product product,
	    String size,
	    int quantity);
     Cart findUserCart(User user);
}
