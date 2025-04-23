/**
 * 
 */
package nana.shop.online.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nana.shop.online.exception.ReviewException;
import nana.shop.online.model.Product;
import nana.shop.online.model.Review;
import nana.shop.online.model.User;
import nana.shop.online.repositories.ReviewRepository;
import nana.shop.online.request.CreateReviewRequest;
import nana.shop.online.service.IReviewService;

/**
 * @author JONATHAN
 */
@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService{

    private final ReviewRepository reviewRepository;

    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product) {
	Review review = new Review();
	review.setUser(user);
	review.setProduct(product);
	review.setReviewText(req.getReviewText());
	review.setRating(req.getReviewRating());
	review.setProductImages(req.getProductImages());
	
	product.getReviews().add(review);
	return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
	
	return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double reviewRating, Long userId) {
	Review review = getReviewById(reviewId);
	if(review.getUser().getId()== reviewId) {
	    review.setReviewText(reviewText);
	    review.setRating(reviewRating);
	    return reviewRepository.save(review);
	}
	throw new ReviewException("You can not update this review");
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) {
	Review review = getReviewById(reviewId);
	if(!review.getUser().getId().equals(userId)) {
	    throw new ReviewException("Oops You can not delete this review!");
	}
	reviewRepository.delete(review);
    }

    @Override
    public Review getReviewById(Long reviewId) {
	
	return reviewRepository.findById(reviewId)
		.orElseThrow(() -> new ReviewException("Review not found"));
    }
    
}
