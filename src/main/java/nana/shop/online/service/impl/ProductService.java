package nana.shop.online.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "productFilters", allEntries = true),
        @CacheEvict(value = "productsBySeller", key = "#seller.id", condition = "#seller != null"),
        @CacheEvict(value = "categories", allEntries = true)
    })
    public Product createProduct(CreateProductRequest req, Seller seller) {
        log.info("Creating new product: {}", req.getTitle());
        
        Category category1 = getOrCreateCategory(req.getCategory(), 1, null);
        Category category2 = getOrCreateCategory(req.getCategory2(), 2, category1);
        Category category3 = getOrCreateCategory(req.getCategory3(), 3, category2);
        
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
        
        Product savedProduct = productRepository.save(product);
        log.info("Product created with ID: {}", savedProduct.getId());
        
        return savedProduct;
    }
    
    private Category getOrCreateCategory(String categoryId, int level, Category parentCategory) {
        Category category = categoryRepository.findByCategoryId(categoryId);
        if (category == null) {
            category = new Category();
            category.setCategoryId(categoryId);
            category.setLevel(level);
            if (parentCategory != null) {
                category.setParentCategory(parentCategory);
            }
            return categoryRepository.save(category);
        }
        return category;
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if (mrpPrice <= 0) {
            throw new IllegalArgumentException("MRP price must be greater than 0");
        }
        
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int) discountPercentage;
    }

    @Override
    @Transactional
    @Caching(
        put = { @CachePut(value = "products", key = "#productId") },
        evict = {
            @CacheEvict(value = "productFilters", allEntries = true),
            @CacheEvict(value = "productSearches", allEntries = true),
            @CacheEvict(value = "productsBySeller", allEntries = true)
        }
    )
    public Product updateProduct(Product product, Long productId) throws ProductException {
        log.info("Updating product with ID: {}", productId);
        
        // Check if product exists
        Product existingProduct = findProductById(productId);
        
        // Update the product
        product.setId(productId);
        
        // If category changed, we need to evict category cache
        if (!existingProduct.getCategory().getId().equals(product.getCategory().getId())) {
            redisTemplate.delete("categories::*");
        }
        
        return productRepository.save(product);
    }

    @Override
    @Cacheable(value = "products", key = "#productId", unless = "#result == null")
    public Product findProductById(Long productId) throws ProductException {
        log.debug("Fetching product with ID: {} (checking cache first)", productId);
        
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            return productOpt.get();
        }
        throw new ProductException("Product not found with id " + productId);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "products", key = "#productId"),
        @CacheEvict(value = "productFilters", allEntries = true),
        @CacheEvict(value = "productSearches", allEntries = true),
        @CacheEvict(value = "productsBySeller", allEntries = true),
        @CacheEvict(value = "categories", allEntries = true)
    })
    public void deleteProduct(Long productId) throws ProductException {
        log.info("Deleting product with ID: {}", productId);
        
        Product product = findProductById(productId);
        productRepository.delete(product);
        
        log.info("Product with ID: {} successfully deleted", productId);
    }

    @Override
    @Cacheable(value = "productSearches", key = "{'search', #query}", unless = "#result.isEmpty()")
    public List<Product> searchProducts(String query) {
        log.debug("Searching products with query: '{}'", query);
        
        return productRepository.searchProduct(query);
    }

    @Override
    @Cacheable(
        value = "productFilters", 
        key = "T(java.util.Objects).hash(#category, #brand, #colors, #sizes, #minPrice, #maxPrice, #minDiscount, #sort, #stock, #pageNumber)",
        unless = "#result.isEmpty()"
    )
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minPrice,
            Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {
        log.debug("Fetching filtered products (checking cache first)");
        
        Specification<Product> specification = buildProductSpecification(category, brand, colors, sizes, minPrice, maxPrice, minDiscount, stock);
        
        Pageable pageable = createPageable(sort, pageNumber);
        
        return productRepository.findAll(specification, pageable);
    }
    
    private Pageable createPageable(String sort, Integer pageNumber) {
        int page = (pageNumber != null) ? pageNumber : 0;
        
        if (sort != null) {
            switch (sort) {
                case "price_low":
                    return PageRequest.of(page, 10, Sort.by("sellingPrice").ascending());
                case "price_high":
                    return PageRequest.of(page, 10, Sort.by("sellingPrice").descending());
                case "newest":
                    return PageRequest.of(page, 10, Sort.by("createdAt").descending());
                case "discount":
                    return PageRequest.of(page, 10, Sort.by("discountPercentage").descending());
                default:
                    return PageRequest.of(page, 10, Sort.unsorted());
            }
        }
        
        return PageRequest.of(page, 10, Sort.by("createdAt").descending());
    }
    
    private Specification<Product> buildProductSpecification(String category, String brand, String colors, String sizes, 
                                                             Integer minPrice, Integer maxPrice, Integer minDiscount, String stock) {
        return (root, query, criteriaBuilder) -> {
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
                predicates.add(criteriaBuilder.like(root.get("sizes").as(String.class), "%" + sizes + "%"));
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
    }

    @Override
    @Cacheable(value = "productsBySeller", key = "#sellerId", unless = "#result.isEmpty()")
    public List<Product> getProductsBySellerId(Long sellerId) {
        log.debug("Fetching products for seller ID: {}", sellerId);
        
        return productRepository.findBySellerId(sellerId);
    }
    
    // Additional methods for cache management
    
    @CacheEvict(value = "products", allEntries = true)
    public void clearProductCache() {
        log.info("Product cache cleared");
    }
    
    @CacheEvict(value = "productFilters", allEntries = true)
    public void clearProductFiltersCache() {
        log.info("Product filters cache cleared");
    }
    
    @CacheEvict(value = "productSearches", allEntries = true)
    public void clearProductSearchCache() {
        log.info("Product search cache cleared");
    }
    
    @Caching(evict = {
        @CacheEvict(value = "products", allEntries = true),
        @CacheEvict(value = "productFilters", allEntries = true),
        @CacheEvict(value = "productSearches", allEntries = true),
        @CacheEvict(value = "productsBySeller", allEntries = true),
        @CacheEvict(value = "categories", allEntries = true)
    })
    public void clearAllCaches() {
        log.info("All product-related caches cleared");
    }
}