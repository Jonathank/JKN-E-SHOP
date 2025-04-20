/**
 * 
 */
package nana.shop.online.service;


import nana.shop.online.model.Seller;
import nana.shop.online.model.SellerReport;

/**
 * @author JONATHAN
 */
public interface ISellerReport {

    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);
    
}
