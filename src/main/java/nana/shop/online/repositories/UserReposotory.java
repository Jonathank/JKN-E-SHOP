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
public interface UserReposotory extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
