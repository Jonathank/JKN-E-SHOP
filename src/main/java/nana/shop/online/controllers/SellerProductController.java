/**
 * 
 */
package nana.shop.online.controllers;

import java.util.List;

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
import nana.shop.online.model.Product;
import nana.shop.online.model.Seller;
import nana.shop.online.request.CreateProductRequest;
import nana.shop.online.service.impl.ProductService;
import nana.shop.online.service.impl.SellerService;

/**
 * @author JONATHAN
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/seller-products")
public class SellerProductController {

    private final ProductService productService;
    private final SellerService sellerService;
    
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Product>> getProductsBySellerId(
	  @RequestHeader("Authorization") String jwt) throws Exception {
	Seller seller = sellerService.getSellerProfile(jwt);
        List<Product> products = productService.getProductsBySellerId(seller.getId());
        return ResponseEntity.ok(products);
    }
    
    @PostMapping("/seller/add/new")
    public ResponseEntity<Product> createProduct(
	  @RequestHeader("Authorization") String jwt,
	  @RequestBody CreateProductRequest request) throws Exception {
	
	Seller seller = sellerService.getSellerProfile(jwt);
	Product product = productService.createProduct(request, seller);
       
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/delete/product/{productId}")
    public ResponseEntity<Product> deleteProduct(
	  @PathVariable Long productId) throws Exception {
	try {
	    productService.deleteProduct(productId);
	    return new ResponseEntity<>(HttpStatus.OK);
	}catch(ProductException e) {
	    return new ResponseEntity<>(HttpStatus.NOT_FOUND);   
	}
    }
    
    @PutMapping("/seller/update/existingproduct/{productId}")
    public ResponseEntity<Product> updateProduct(
	  @PathVariable Long  productId,
	  @RequestBody Product product) throws Exception {
	try {
	   Product updatedProduct = productService.updateProduct(product, productId);
	   
	    return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
	}catch(ProductException e) {
	    return new ResponseEntity<>(HttpStatus.NOT_FOUND);   
	}
    }
    
}
