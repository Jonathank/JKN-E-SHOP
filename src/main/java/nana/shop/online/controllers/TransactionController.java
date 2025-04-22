/**
 * 
 */
package nana.shop.online.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nana.shop.online.model.Seller;
import nana.shop.online.model.Transaction;
import nana.shop.online.service.impl.SellerService;
import nana.shop.online.service.impl.TransactionService;

/**
 * @author JONATHAN
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final SellerService sellerService;
    
    @GetMapping("/by/seller")
    public ResponseEntity<List<Transaction>>getTransactionBySeller(
	    @RequestHeader("Authorization") String jwt
	    ) throws Exception{
	Seller seller = sellerService.getSellerProfile(jwt);
	List<Transaction> transactions = transactionService.getTransactionsBySellerId(seller);
	return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/get/all")
    public ResponseEntity<List<Transaction>>getAllTransactions(){
	List<Transaction> transactions = transactionService.getAllTransactions();
	return ResponseEntity.ok(transactions);
    }
}
