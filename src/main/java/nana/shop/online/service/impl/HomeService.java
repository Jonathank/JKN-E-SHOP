/**
 * Core service implementation for the online shop's home page functionality.
 * This class is responsible for organizing and assembling different sections
 * of the home page by filtering categories based on their designated sections.
 */
package nana.shop.online.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nana.shop.online.domain.HomeCategorySection;
import nana.shop.online.model.Deal;
import nana.shop.online.model.Home;
import nana.shop.online.model.HomeCategory;
import nana.shop.online.repositories.DealRepository;
import nana.shop.online.service.IHomeService;

/**
 * @author JONATHAN 
 */
@Service 
@RequiredArgsConstructor 
public class HomeService implements IHomeService {
    
    private final DealRepository dealRepository;
  
    /**
     * Creates the complete data structure for the home page by organizing categories
     * into their respective sections and retrieving or creating deal information.
     *
     * @param allCategories A complete list of all available categories in the system
     * @return A fully populated Home object with categories organized by sections
     */
    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {
        /**
         * Filter categories designated for the grid section of the home page
         */
        List<HomeCategory> gridCategories = allCategories.stream()
                .filter(category -> category.getSecction() == HomeCategorySection.GRID)
                .collect(Collectors.toList());
        
        /**
         * Filter categories designated for the shop-by-category section
         */
        List<HomeCategory> shopByCategories = allCategories.stream()
                .filter(category -> category.getSecction() == HomeCategorySection.SHOP_BY_CATEGORY)
                .collect(Collectors.toList());
        
        /**
         * Filter categories designated for the electronics section
         */
        List<HomeCategory> electricCategories = allCategories.stream()
                .filter(category -> category.getSecction() == HomeCategorySection.ELECTRIC_CATEGORY)
                .collect(Collectors.toList());
        
        /**
         * Filter categories designated for the deals section
         */
        List<HomeCategory> dealCategories = allCategories.stream()
                .filter(category -> category.getSecction() == HomeCategorySection.DEALS)
                .collect(Collectors.toList());
        
        /**
         * List to hold the deals that will be displayed on the home page
         */
        List<Deal> createDeals = new ArrayList<>();
        
        /**
         * Check if deals already exist in the database
         * If not, create new deals with 10% discount for each deal category
         * Otherwise, use existing deals from the database
         */
        if(dealRepository.findAll().isEmpty()) {
            // Create new deals with 10% discount for categories in the DEALS section
            List<Deal> deals = allCategories.stream()
                    .filter(category -> category.getSecction() == HomeCategorySection.DEALS)
                    .map(category -> new Deal(null, 10, category))
                    .collect(Collectors.toList());
            
            // Save the newly created deals to the database
            createDeals = dealRepository.saveAll(deals);
            
        } else {
            // Use existing deals from the database
            createDeals = dealRepository.findAll();
        }
        
        /**
         * Create and populate the Home object with organized category sections
         * and deal information for rendering the complete home page
         */
        Home home = new Home();
        home.setGrid(gridCategories);
        home.setShopByCategories(shopByCategories);
        home.setElectricCategories(electricCategories);
        home.setDealCategories(dealCategories);
        home.setDeals(createDeals);
        
        return home;
    }
}