/**
 * 
 */
package nana.shop.online.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.model.Cart;

/**
 * @author JONATHAN
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    
//    Cart findByUser(User user);
//
//    Cart findByUserAndStatus(User user, String status);
//
//    Cart findByUserAndStatusAndProduct(User user, String status, Product product);
//
//    Cart findByUserAndStatusAndProductAndSize(User user, String status, Product product, String size);
//
//    Cart findByUserAndStatusAndProductAndSizeAndColor(User user, String status, Product product, String size,
//	    String color);
//
//    List<Cart> findByUserAndStatusAndProductAndSizeAndColorAndQuantity(User user, String status, Product product,
//	    String size, String color, int quantity);
//
//    List<Cart> findByUserAndStatusAndProductAndSizeAndColorAndQuantityAndPrice(User user, String status,
//	    Product product, String size, String color, int quantity, double price);
//
//    List<Cart> findByUserAndStatusAndProductAndSizeAndColorAndQuantityAndPriceAndTotal(User user, String status,
//	    Product product, String size, String color, int quantity, double price, double total);
//
//    List<Cart> findByUserAndStatusAndProductAndSizeAndColorAndQuantityAndPriceAndTotalAndImage(User user, String status,
//	    Product product, String size, String color, int quantity, double price, double total, String image);
//
//    List<Cart> findByUserAndStatusAndProductAndSizeAndColorAndQuantityAndPriceAndTotalAndImageAndProductName(User user,
//	    String status, Product product, String size, String color, int quantity, double price, double total,
//	    String image, String productName);
//
//    List<Cart> findByUserAndStatusAndProductAndSizeAndColorAndQuantityAndPriceAndTotalAndImageAndProductNameAndCategory(
//	    User user, String status, Product product, String size, String color, int quantity, double price,
//	    double total, String image, String productName, String category);
//
//    List<Cart> findByUserAndStatusAndProductAndSizeAndColorAndQuantityAndPriceAndTotalAndImageAndProductNameAndCategoryAndBrand(
//	    User user, String status, Product product, String size, String color, int quantity, double price,
//	    double total, String image, String productName, String category, String brand);
//
//    List<Cart> findByUserAndStatusAndProductAndSizeAndColorAndQuantityAndPriceAndTotalAndImageAndProductNameAndCategoryAndBrandAndDescription(User user, String status, Product product, String size, String color

}
