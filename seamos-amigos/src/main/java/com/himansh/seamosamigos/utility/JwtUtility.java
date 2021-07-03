package com.himansh.seamosamigos.utility;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtility {
	
    //Extracting and Validating JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    //Custom Addition check
    public String extractClaimValue(String token, String claimKey) {
        return extractClaim(token, c->c.get(claimKey, String.class));
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(AmigosConstants.SECRET_KEY).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }
    
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public Boolean validateToken(String token, UserDetails userDetails, String requestIp) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) //&& !isTokenExpired(token) 
        		&& requestIp.equals(extractClaimValue(token, "clientIp")));
    }
    
	//Generating JWT
    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        return createToken(claims, userDetails.getUsername());
    }

	private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
        		.signWith(SignatureAlgorithm.HS256, AmigosConstants.SECRET_KEY).compact();
                //.setExpiration(new Date(System.currentTimeMillis() + AmigosConstants.TOKEN_EXPIRY_LIMIT))
    }

}
