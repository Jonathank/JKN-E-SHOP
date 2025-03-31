/**
 * 
 */
package nana.shop.online.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.model.Address;

/**
 * @author JONATHAN
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

  //  Address findByCity(String city);

}
