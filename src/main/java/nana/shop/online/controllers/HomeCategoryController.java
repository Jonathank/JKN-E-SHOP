/**
 * 
 */
package nana.shop.online.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nana.shop.online.exception.HomeCategoryException;
import nana.shop.online.model.Home;
import nana.shop.online.model.HomeCategory;
import nana.shop.online.service.impl.HomeCategoryService;
import nana.shop.online.service.impl.HomeService;

/**
 * @author JONATHAN
 */
@RestController
@RequestMapping("${api.prefix}/homeCategories")
@RequiredArgsConstructor
public class HomeCategoryController {

    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;
    
    @PostMapping("/admin/home/categories/create")
    public ResponseEntity<Home> createHomeCategories(
	    @RequestBody List<HomeCategory> homeCategories
	    ){
	List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
	Home home = homeService.createHomePageData(categories);
	return new ResponseEntity<>(home, HttpStatus.ACCEPTED);
    }
    
    @GetMapping("/admin/home/categories/get")
    public ResponseEntity<List<HomeCategory>> getHomeCategories(
	    ){
	List<HomeCategory> categories = homeCategoryService.getAllHomeCategories();
	
	return new ResponseEntity<>(categories, HttpStatus.ACCEPTED);
    }
    
    @PatchMapping("/admin/home/categories/update/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(
	    @PathVariable Long id,
	    @RequestBody HomeCategory homeCategory
	    ) throws HomeCategoryException{
	HomeCategory updatedCategory = homeCategoryService.updateHomeCategory(homeCategory, id);
	
	return new ResponseEntity<>(updatedCategory, HttpStatus.ACCEPTED);
    }
    
    
}
