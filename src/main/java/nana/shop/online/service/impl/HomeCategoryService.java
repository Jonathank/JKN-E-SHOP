/**
 * 
 */
package nana.shop.online.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nana.shop.online.exception.HomeCategoryException;
import nana.shop.online.model.HomeCategory;
import nana.shop.online.repositories.HomeCategoryRepository;
import nana.shop.online.service.IHomeCategoryService;

/**
 * @author JONATHAN
 */
@Service
@RequiredArgsConstructor
public class HomeCategoryService implements IHomeCategoryService{

    private final HomeCategoryRepository homeCategoryRepository;
    
    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
	
	return homeCategoryRepository.save(homeCategory);
    }

    @Override
    public List<HomeCategory> createCategories(List<HomeCategory> homeCategories) {
	if(homeCategoryRepository.findAll().isEmpty()) {
	    return homeCategoryRepository.saveAll(homeCategories);
	}
	return homeCategoryRepository.findAll();
    }

    @Override
    public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws HomeCategoryException {
	HomeCategory existingCategory = homeCategoryRepository.findById(id)
		.orElseThrow(() -> new HomeCategoryException("Category not found"));
	if(homeCategory.getImage() != null) {
	    existingCategory.setImage(homeCategory.getImage());
	}
	if(homeCategory.getCategoryId() != null) {
	    existingCategory.setCategoryId(homeCategory.getCategoryId());
	}
	return homeCategoryRepository.save(existingCategory);
    }

    @Override
    public List<HomeCategory> getAllHomeCategories() {
	
	return homeCategoryRepository.findAll();
    }
    

}
