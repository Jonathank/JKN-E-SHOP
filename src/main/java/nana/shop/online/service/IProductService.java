/**
 * 
 */
package nana.shop.online.service;



import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;

import nana.shop.online.exception.ProductException;
import nana.shop.online.model.Product;
import nana.shop.online.model.Seller;
import nana.shop.online.request.CreateProductRequest;

/**
 * @author JONATHAN
 */
public interface IProductService {

    Product createProduct(CreateProductRequest req, Seller seller);
    Product updateProduct(Product product, Long productId) throws ProductException;
    Product findProductById(Long productId) throws ProductException;
    void deleteProduct(Long productId) throws ProductException;
    Page<Product>getAllProducts(
	    String category,
	    String brand,
	    String colors,
	    String sizes,
	    Integer minPrice,
	    Integer maxPrice,
	    Integer minDiscount,
	    String sort,
	    String stock,
	    Integer pageNumber);
    List<Product>getProductsBySellerId(Long sellerId);
    /**
     * @param query
     * @return
     */
    List<Product> searchProducts(String query);
    
}
