/**
 * 
 */
package nana.shop.online.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nana.shop.online.model.User;

/**
 * @author JONATHAN
 * 
 */
public interface UserRepository extends JpaRepository<User, Long> {

   User findByEmail(String email);

}
