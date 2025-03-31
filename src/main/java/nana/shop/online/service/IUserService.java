/**
 * 
 */
package nana.shop.online.service;

import nana.shop.online.model.User;

/**
 * @author JONATHAN
 */
public interface IUserService {

    User findUserByEmail(String email) throws Exception;
    User findUserByJwtToken(String jwtToken) throws Exception;
}
