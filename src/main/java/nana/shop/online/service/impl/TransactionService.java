/**
 * 
 */
package nana.shop.online.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nana.shop.online.exception.SellerException;
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

public class TransactionService implements ITransactionService{

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;
    
    @Override
    public Transaction createTransaction(Order order) throws SellerException {
	Seller seller = sellerRepository.findById(order.getSellerId())
		    .orElseThrow(() -> new SellerException("Seller not found"));

	Transaction transaction = new Transaction();
	transaction.setSeller(seller);
	transaction.setCustomer(order.getUser());
	transaction.setOrder(order);
	
	return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsBySellerId(Seller seller) {
	
	return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
	
	return transactionRepository.findAll();
    }

}
