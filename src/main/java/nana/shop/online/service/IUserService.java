/**
 * 
 */
package nana.shop.online.service;

import nana.shop.online.exception.UserException;
import nana.shop.online.model.User;

/**
 * @author JONATHAN
 */
public interface IUserService {

    User findUserByEmail(String email) throws UserException;
    User findUserByJwtToken(String jwtToken) throws UserException;
}
