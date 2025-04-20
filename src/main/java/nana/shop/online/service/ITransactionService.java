/**
 * 
 */
package nana.shop.online.service;

import java.util.List;

import nana.shop.online.model.Order;
import nana.shop.online.model.Seller;
import nana.shop.online.model.Transaction;

/**
 * @author JONATHAN
 */
public interface ITransactionService {

    Transaction createTransaction(Order order);
    List<Transaction>geTransactionsBySellerId(Seller seller);
    List<Transaction>getAllTransactions();
}
