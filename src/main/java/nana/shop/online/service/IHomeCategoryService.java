/**
 * 
 */
package nana.shop.online.service;

import java.util.List;

import nana.shop.online.exception.HomeCategoryException;
import nana.shop.online.model.HomeCategory;

/**
 * @author JONATHAN
 */
public interface IHomeCategoryService {

    HomeCategory createHomeCategory(HomeCategory homeCategory);
    List<HomeCategory>createCategories(List<HomeCategory> homeCategories);
    HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws HomeCategoryException;
    List<HomeCategory>getAllHomeCategories();
    
}
