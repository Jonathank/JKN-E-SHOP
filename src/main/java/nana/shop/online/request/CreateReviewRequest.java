/**
 * 
 */
package nana.shop.online.request;

import java.util.List;

import lombok.Data;

/**
 * @author JONATHAN
 */
@Data
public class CreateReviewRequest {

    private String reviewText;
    private double reviewRating;
    private List<String> productImages;
}
