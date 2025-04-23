/**
 * 
 */
package nana.shop.online.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;
import nana.shop.online.exception.ProductException;
import nana.shop.online.exception.UserException;
import nana.shop.online.model.Product;
import nana.shop.online.model.User;
import nana.shop.online.model.WishList;
import nana.shop.online.service.impl.ProductService;
import nana.shop.online.service.impl.UserService;
import nana.shop.online.service.impl.WishListService;

/**
 * @author JONATHAN
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/whisList")
public class WishListController {
    private final WishListService wishListService;
    private final UserService userService;
    private final ProductService productService;
    
    @GetMapping("/get/by-user")
    public ResponseEntity<WishList>getWishListByUserId(
	   @RequestHeader("Authorization") String jwt
	    ) throws UserException{
	User user = userService.findUserByJwtToken(jwt);
	
	WishList wishList = wishListService.getWishListByUserId(user);
	return new ResponseEntity<>(wishList, HttpStatus.OK);
    }
    
    @PostMapping("/add-product/{productId}")
    public ResponseEntity<WishList>getWishListByUserId(
	   @RequestHeader("Authorization") String jwt,
	   @PathVariable Long productId
	    ) throws UserException, ProductException{
	Product product = productService.findProductById(productId);
	User user = userService.findUserByJwtToken(jwt);
	WishList updatedwishList = wishListService.addProductToWishList(user, product);
	return new ResponseEntity<>(updatedwishList, HttpStatus.OK);
    }

}
