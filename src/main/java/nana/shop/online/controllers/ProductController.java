/**
 * 
 */
package nana.shop.online.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nana.shop.online.exception.ProductException;
import nana.shop.online.model.Product;
import nana.shop.online.model.Seller;
import nana.shop.online.request.CreateProductRequest;
import nana.shop.online.service.impl.ProductService;

/**
 * @author JONATHAN
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create/product")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request, @RequestParam Seller seller) {
        Product product = productService.createProduct(request, seller);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}/product")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) throws ProductException {
        Product updatedProduct = productService.updateProduct(product, id);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) throws ProductException {
        Product product = productService.findProductById(id);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/delete/{id}/product")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) throws ProductException {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/search/product")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam(required = false) String query) {
        List<Product> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/all/get")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String colors,
            @RequestParam(required = false) String sizes,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String stock,
            @RequestParam(defaultValue = "0") Integer pageNumber) {

        Page<Product> products = productService.getAllProducts(
                category, brand, colors, sizes, minPrice, maxPrice, minDiscount, sort, stock, pageNumber
        );
        return ResponseEntity.ok(products);
    }

    
}

