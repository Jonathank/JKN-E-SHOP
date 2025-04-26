/**
 * 
 */
package nana.shop.online.service;

import java.util.List;

import nana.shop.online.model.Home;
import nana.shop.online.model.HomeCategory;

/**
 * @author JONATHAN
 */
public interface IHomeService {

    Home createHomePageData(List<HomeCategory> allCategories);
}
