/**
 * 
 */
package nana.shop.online.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nana.shop.online.model.Product;
import nana.shop.online.model.User;
import nana.shop.online.model.WishList;
import nana.shop.online.repositories.WishListRepository;
import nana.shop.online.service.IWishListService;

/**
 * @author JONATHAN
 */
@Service
@RequiredArgsConstructor
public class WishListService implements IWishListService{

    private WishListRepository wishListRepository;
    
    @Override
    public WishList createWishList(User user) {
	WishList wishList = new WishList();
	wishList.setUser(user);
	return wishListRepository.save(wishList);
    }

    @Override
    public WishList getWishListByUserId(User user) {
	
	WishList wishList= wishListRepository.findByUserId(user.getId());
	if(wishList == null) {
	    wishList = createWishList(user);
	}
	return wishList;
    }

    @Override
    public WishList addProductToWishList(User user, Product product) {
	WishList wishList = getWishListByUserId(user);
	
	if(wishList.getProducts().contains(product)) {
	    wishList.getProducts().remove(product);
	}
	else {
	    wishList.getProducts().add(product);
	}
	return wishListRepository.save(wishList);
    }

}
