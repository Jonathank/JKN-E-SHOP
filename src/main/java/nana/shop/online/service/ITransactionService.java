/**
 * 
 */
package nana.shop.online.service;

import java.util.List;

import nana.shop.online.exception.SellerException;
import nana.shop.online.model.Order;
import nana.shop.online.model.Seller;
import nana.shop.online.model.Transaction;

/**
 * @author JONATHAN
 */
public interface ITransactionService {

    Transaction createTransaction(Order order) throws SellerException;
    List<Transaction>getAllTransactions();
    /**
     * @param seller
     * @return
     */
    List<Transaction> getTransactionsBySellerId(Seller seller);
}
