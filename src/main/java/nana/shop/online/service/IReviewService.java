/**
 * 
 */
package nana.shop.online.service;

import java.util.List;

import nana.shop.online.model.Product;
import nana.shop.online.model.Review;
import nana.shop.online.model.User;
import nana.shop.online.request.CreateReviewRequest;

/**
 * @author JONATHAN
 */
public interface IReviewService {

    Review createReview(
	    CreateReviewRequest req,
	    User user,
	    Product product
	    );
    List<Review> getReviewByProductId(Long productId);
    Review updateReview(Long reviewId, String reviewText,double reviewRating,Long userId);
    void deleteReview(Long reviewId, Long userId);
    Review getReviewById(Long reviewId);
}
