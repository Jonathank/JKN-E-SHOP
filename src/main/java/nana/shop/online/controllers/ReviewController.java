/**
 * 
 */
package nana.shop.online.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import nana.shop.online.model.Review;
import nana.shop.online.model.User;
import nana.shop.online.request.CreateReviewRequest;
import nana.shop.online.response.ApiResponse;
import nana.shop.online.service.impl.ProductService;
import nana.shop.online.service.impl.ReviewService;
import nana.shop.online.service.impl.UserService;

/**
 * @author JONATHAN
 */
@RestController
@RequestMapping("${api.prefix}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>>  getReviewsByProductId(
	    @PathVariable Long productId){
	List<Review> reviews = reviewService.getReviewByProductId(productId);
	
	return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
       
    @PostMapping("/write/product/{productId}/review")
    public ResponseEntity<Review>  writeReview(
	    @RequestHeader("Authorization") String jwt,
	    @RequestBody CreateReviewRequest req,
	    @PathVariable Long productId) throws UserException, ProductException{
	User user = userService.findUserByJwtToken(jwt);
	Product product = productService.findProductById(productId);
	Review review = reviewService.createReview(req, user, product);
	
	return new ResponseEntity<>(review, HttpStatus.OK);
    }
    
    @PatchMapping("/update/review/{reviewId}")
    public ResponseEntity<Review>  updateReview(
	    @RequestHeader("Authorization") String jwt,
	    @RequestBody CreateReviewRequest req,
	    @PathVariable Long reviewId) throws UserException, ProductException{
	User user = userService.findUserByJwtToken(jwt);
	
	Review review = reviewService.updateReview(
		reviewId, 
		req.getReviewText(), 
		req.getReviewRating(), 
		user.getId());
	
	return new ResponseEntity<>(review, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/review/{reviewId}")
    public ResponseEntity<ApiResponse>  deleteReview(
	    @RequestHeader("Authorization") String jwt,
	    @PathVariable Long reviewId) throws UserException, ProductException{
	User user = userService.findUserByJwtToken(jwt);
	reviewService.deleteReview(reviewId, user.getId());
	
	ApiResponse response = new ApiResponse();
	response.setMessage("Review deleted Successfully");

	return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
}
