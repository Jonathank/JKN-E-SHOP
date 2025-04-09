/**
 * 
 */
package nana.shop.online.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nana.shop.online.exception.ProductException;
import nana.shop.online.exception.UserException;
import nana.shop.online.model.Cart;
import nana.shop.online.model.CartItems;
import nana.shop.online.model.Product;
import nana.shop.online.model.User;
import nana.shop.online.request.AddItemRequest;
import nana.shop.online.response.ApiResponse;
import nana.shop.online.service.impl.CartItemService;
import nana.shop.online.service.impl.CartService;
import nana.shop.online.service.impl.ProductService;
import nana.shop.online.service.impl.UserService;

/**
 * @author JONATHAN
 */
@RestController
@RequestMapping("${api.prefix}/cart")
@RequiredArgsConstructor
public class CartController {

     private final CartService cartService;
     private final CartItemService cartItemService;
     private final UserService userService;
    private final ProductService productService;
     
     @GetMapping("/view/user-cart")
      public ResponseEntity<Cart> findUserCartHandler(
	      @RequestHeader("Authorization") String jwt)throws UserException {
     User user = userService.findUserByJwtToken(jwt);
     Cart cart = cartService.findUserCart(user);
      return new ResponseEntity<>(cart, HttpStatus.OK);
      }
     
     @PutMapping("/add-to-cart")
     public ResponseEntity<CartItems> addItemToCart(
	     @RequestHeader("Authorization") String jwt,
	     @RequestBody AddItemRequest req) throws UserException,ProductException{
	     
	 User user = userService.findUserByJwtToken(jwt);
         Product product = productService.findProductById(req.getProductId());
         CartItems cartItem = cartService.addCartItem(user, product, req.getSize(), req.getQuantity());
             
             ApiResponse response = new ApiResponse();
             response.setMessage("Item added to cart successfully");
                    
        return new ResponseEntity<>(cartItem, HttpStatus.ACCEPTED);
     }
     
     @DeleteMapping("/remove-from-cart/{cartItemId}")
     public ResponseEntity<ApiResponse> removeItemFromCart(
         @RequestHeader("Authorization") String jwt,
         @PathVariable Long cartItemId) throws UserException{
	             User user = userService.findUserByJwtToken(jwt);
	             cartItemService.removeCartItems(user.getId(), cartItemId);
	             ApiResponse response = new ApiResponse();
	             response.setMessage("Item removed from cart successfully");
	             return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	    
     }
     
     @PutMapping("/update-cart/{cartItemId}")
     public ResponseEntity<CartItems> updateCartItem(
	     @RequestHeader("Authorization") String jwt,
	     @PathVariable Long cartItemId,
	     @RequestBody CartItems cartItem) throws UserException {
	 User user = userService.findUserByJwtToken(jwt);
	 CartItems updateCartItem = null;
	 if(cartItem.getQuantity() > 0) {
	   updateCartItem = cartItemService
		   .updateCartItems(user.getId(), cartItemId, cartItem);
	 }
	 ApiResponse response = new ApiResponse();
	 response.setMessage("Item updated successfully");
	
	 return new ResponseEntity<>(updateCartItem, HttpStatus.ACCEPTED);
     }
}
