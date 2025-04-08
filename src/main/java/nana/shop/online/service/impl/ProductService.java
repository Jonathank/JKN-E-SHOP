/**
 * 
 */
package nana.shop.online.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import nana.shop.online.exception.ProductException;
import nana.shop.online.model.Category;
import nana.shop.online.model.Product;
import nana.shop.online.model.Seller;
import nana.shop.online.repositories.CategoryRepository;
import nana.shop.online.repositories.ProductRepository;
import nana.shop.online.request.CreateProductRequest;
import nana.shop.online.service.IProductService;

/**
 * @author JONATHAN
 */
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) {

	Category category1 = categoryRepository.findByCategoryId(req.getCategory());
	if(category1 == null) {
	    Category category = new Category();
	    category.setCategoryId(req.getCategory());
	    category.setLevel(1);
	    category1 = categoryRepository.save(category);
	}
	
	Category category2 = categoryRepository.findByCategoryId(req.getCategory2());
	if(category2 == null) {
	    Category category = new Category();
	    category.setCategoryId(req.getCategory2());
	    category.setLevel(2);
	    category.setParentCategory(category1);
	    category2 = categoryRepository.save(category);
	}
	
	Category category3 = categoryRepository.findByCategoryId(req.getCategory3());
	if(category3 == null) {
	    Category category = new Category();
	    category.setCategoryId(req.getCategory3());
	    category.setLevel(3);
	    category.setParentCategory(category2);
	    category3 = categoryRepository.save(category);
	}
	
	int discountPercentage = calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());
	
	Product product = new Product();
	product.setSeller(seller);
	product.setCategory(category3);
	product.setDescription(req.getDescription());
	product.setCreatedAt(LocalDateTime.now());
	product.setTitle(req.getTitle());
	product.setColor(req.getColor());
	product.setSellingPrice(req.getSellingPrice());
	product.setImages(req.getImages());
	product.setMrpPrice(req.getMrpPrice());
	product.setSizes(req.getSizes());
	product.setDiscountPercentage(discountPercentage);
	
	return productRepository.save(product);
    }

    /**
     * @param mrpPrice
     * @param sellingPrice
     * @return
     */
    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
	if(mrpPrice <=0) {
	    throw new IllegalArgumentException("Actual price must be greater than 0");
	}
	
	double discount = mrpPrice - sellingPrice;
	double discountPercentage = (discount/mrpPrice)*100;
	return (int) discountPercentage;
    }

    @Override
    public Product updateProduct(Product product, Long productId) throws ProductException {
	findProductById(productId);
	product.setId(productId);
	
	return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
	return productRepository.findById(productId)
		.orElseThrow(() -> new ProductException("product not found with id "+ productId));
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
	Product product = findProductById(productId);
	productRepository.delete(product);
    }

    @Override
    public List<Product> searchProducts(String query) {
	
	return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minPrice,
	        Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {

	    Specification<Product> specification = (root, query, criteriaBuilder) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        // Category filter
	        if (category != null && !category.isEmpty()) {
	            Join<Product, Category> categoryJoin = root.join("category");
	            predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
	        }

	        // Brand filter
	        if (brand != null && !brand.isEmpty()) {
	            predicates.add(criteriaBuilder.equal(root.get("brand"), brand));
	        }

	        // Color filter
	        if (colors != null && !colors.isEmpty()) {
	            predicates.add(criteriaBuilder.equal(root.get("color"), colors));
	        }

	        // Size filter
	        if (sizes != null && !sizes.isEmpty()) {
	            predicates.add(criteriaBuilder.equal(root.get("size"), sizes));
	        }

	        // Price range filter
	        if (minPrice != null) {
	            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
	        }
	        if (maxPrice != null) {
	            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
	        }

	        // Minimum discount filter
	        if (minDiscount != null) {
	            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercentage"), minDiscount));
	        }

	        // Stock availability filter
	        if (stock != null) {
	            predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
	        }

	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	    };

	    Pageable pageable;
	    if (sort != null) {
	        switch (sort) {
	            case "price_low":
	                pageable = PageRequest.of((pageNumber != null ? pageNumber : 0), 10, Sort.by("sellingPrice").ascending());
	                break;
	            case "price_high":
	                pageable = PageRequest.of((pageNumber != null ? pageNumber : 0), 10, Sort.by("sellingPrice").descending());
	                break;
//	            case "newest":
//	                pageable = PageRequest.of((pageNumber != null ? pageNumber : 0), 10, Sort.by("createdAt").descending());
//	                break;
	            default:
	        	pageable = PageRequest.of((pageNumber != null ? pageNumber : 0), 10, Sort.unsorted());
	                break;
	        }
	    }  else {
		    pageable = PageRequest.of((pageNumber != null ? pageNumber : 0), 10, Sort.unsorted());
	    }

	    return productRepository.findAll(specification, pageable);
	}


    @Override
    public List<Product> getProductsBySellerId(Long sellerId) {
	
	return productRepository.findBySellerId(sellerId);
    }

}
