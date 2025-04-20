/**
 * 
 */
package nana.shop.online.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nana.shop.online.model.Order;
import nana.shop.online.model.Seller;
import nana.shop.online.model.Transaction;
import nana.shop.online.repositories.SellerRepository;
import nana.shop.online.repositories.TransactionRepository;
import nana.shop.online.service.ITransactionService;

/**
 * @author JONATHAN
 */
@Service
@RequiredArgsConstructor

public class TransactinService implements ITransactionService{

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;
    
    @Override
    public Transaction createTransaction(Order order) {
	
	return null;
    }

    @Override
    public List<Transaction> geTransactionsBySellerId(Seller seller) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Transaction> getAllTransactions() {
	// TODO Auto-generated method stub
	return null;
    }

}
