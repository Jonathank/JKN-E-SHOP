/**
 * 
 */
package nana.shop.online.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.model.Category;

/**
 * 
 */
public interface CategoryRepository extends JpaRepository<Category, Long>{

    /**
     * @param category
     * @return
     */
    Category findByCategoryId(String category);

}
