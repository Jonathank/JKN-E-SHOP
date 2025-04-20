/**
 * 
 */
package nana.shop.online.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.model.Transaction;

/**
 * @author JONATHAN
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

}
