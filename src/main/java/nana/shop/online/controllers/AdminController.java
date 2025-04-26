/**
 * 
 */
package nana.shop.online.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nana.shop.online.domain.AccountStatus;
import nana.shop.online.model.Seller;
import nana.shop.online.service.impl.SellerService;

/**
 * @author JONATHAN
 */
@RestController
@RequestMapping("${api.prefix}/Admin")
@RequiredArgsConstructor
public class AdminController {

    private final SellerService sellerService;
    
   @PatchMapping("/update/seller/{id}/status/{status}")
   public ResponseEntity<Seller>updateSellerStatus(
	   @PathVariable Long id,
	   @PathVariable AccountStatus status
	   ) throws Exception{
       Seller updatedSeller = sellerService.updateSellerAccountStatus(id, status);
       return ResponseEntity.ok(updatedSeller);
   }
}
