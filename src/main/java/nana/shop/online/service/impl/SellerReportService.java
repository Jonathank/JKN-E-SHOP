/**
 * 
 */
package nana.shop.online.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nana.shop.online.model.Seller;
import nana.shop.online.model.SellerReport;
import nana.shop.online.repositories.SellerReportRepository;
import nana.shop.online.service.ISellerReport;

/**
 * @author JONATHAN
 */
@Service
@RequiredArgsConstructor
public class SellerReportService implements ISellerReport{
    
    private final SellerReportRepository sellerReportRepository;
    
    @Override
    public SellerReport getSellerReport(Seller seller) {
	SellerReport sellerReport = sellerReportRepository.findBySellerId(seller.getId());
	if(sellerReport == null) {
	    SellerReport newrReport = new SellerReport();
	    newrReport.setSeller(seller);
	    return sellerReportRepository.save(newrReport);
	}
	return sellerReport;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
	
	return sellerReportRepository.save(sellerReport);
    }

  
}
