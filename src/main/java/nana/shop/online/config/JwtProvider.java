/**
 * 
 */
package nana.shop.online.config;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * @author JONATHAN
 */
@Service
public class JwtProvider {

    SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes());
    
    public String generateToken(Authentication authentication) {
	Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	String roles = populateAuthorities(authorities);
	return  Jwts.builder()
		.setIssuedAt(new Date())
		.setExpiration(new Date(new Date().getTime() + 864000000))
		.claim("email", authentication.getName())
		.claim("authorities", roles)
		.signWith(key)
		.compact();
	
    }

    /**
     * @param authorities
     * @return
     */
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
	Set<String>auths = new HashSet<>();
	
	for (GrantedAuthority authority : authorities) {
	    auths.add(authority.getAuthority());
	}
	return String.join(",", auths);
    }
    
    public String getEmailFromToken(String token) {
	token = token.substring(7);
	Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
		.parseClaimsJws(token).getBody();
	return String.valueOf(claims.get("email"));
    }

   
    
}