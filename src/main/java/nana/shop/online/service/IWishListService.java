/**
 * 
 */
package nana.shop.online.service;

import nana.shop.online.model.Product;
import nana.shop.online.model.User;
import nana.shop.online.model.WishList;

/**
 * @author JONATHAN
 */
public interface IWishListService {

    WishList createWishList(User user);
    WishList getWishListByUserId(User user);
    WishList addProductToWishList(User user, Product product);
}
