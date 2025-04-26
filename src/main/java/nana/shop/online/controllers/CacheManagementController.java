/**
 * 
 */
package nana.shop.online.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.actuate.cache.CachesEndpoint;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nana.shop.online.service.impl.ProductService;
/**
 * @author JONATHAN
 */
@RestController
@RequestMapping("${api.prefix}/admin/cache")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class CacheManagementController {

    private final CacheManager cacheManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductService productService;
    private final CachesEndpoint cachesEndpoint;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCacheStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // Get all cache names
        cacheManager.getCacheNames().forEach(cacheName -> {
            Set<String> keys = redisTemplate.keys(cacheName + "*");
            int size = keys != null ? keys.size() : 0;
            statistics.put(cacheName, Map.of(
                "size", size, 
                "keys", keys != null ? keys : Set.of()
            ));
        });
        
        return ResponseEntity.ok(statistics);
    }
    
    @GetMapping("/cache-report")
    public ResponseEntity<Object> getCacheReport() {
        return ResponseEntity.ok(cachesEndpoint.caches());
    }

    @DeleteMapping("/{cacheName}")
    public ResponseEntity<Map<String, String>> clearCache(@PathVariable String cacheName) {
        if (cacheManager.getCache(cacheName) != null) {
            cacheManager.getCache(cacheName).clear();
            log.info("Cache '{}' cleared successfully", cacheName);
            return ResponseEntity.ok(Map.of("status", "Cache " + cacheName + " cleared successfully"));
        } else {
            log.warn("Cache '{}' not found", cacheName);
            return ResponseEntity.badRequest().body(Map.of("error", "Cache not found"));
        }
    }
    
    @DeleteMapping("/products")
    public ResponseEntity<Map<String, String>> clearProductCache() {
        productService.clearProductCache();
        return ResponseEntity.ok(Map.of("status", "Product cache cleared successfully"));
    }
    
    @DeleteMapping("/products/filters")
    public ResponseEntity<Map<String, String>> clearFilterCache() {
        productService.clearProductFiltersCache();
        return ResponseEntity.ok(Map.of("status", "Product filters cache cleared successfully"));
    }
    
    @DeleteMapping("/products/search")
    public ResponseEntity<Map<String, String>> clearSearchCache() {
        productService.clearProductSearchCache();
        return ResponseEntity.ok(Map.of("status", "Product search cache cleared successfully"));
    }
    
    @DeleteMapping
    public ResponseEntity<Map<String, String>> clearAllCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            cacheManager.getCache(cacheName).clear();
            log.info("Cache '{}' cleared successfully", cacheName);
        });
        
        return ResponseEntity.ok(Map.of("status", "All caches cleared successfully"));
    }
}