package com.himansh.seamosamigos.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.himansh.seamosamigos.utility.AmigosConstants;
import com.himansh.seamosamigos.utility.CurrentUser;
import com.himansh.seamosamigos.utility.JwtUtility;
import com.himansh.seamosamigos.utility.Utilities;

import io.jsonwebtoken.JwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtility jwtUtil;
	@Autowired
	private Utilities utilities;
    
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
        String username = null;
        String jwt = null;
        String errMessage = "";
        
        final String authorizationHeader = request.getHeader("Authorization");
        try {
	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            jwt = authorizationHeader.substring(7);
	            username = jwtUtil.extractUsername(jwt);
	        }
	        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        	UserPrincipal userDetails=(UserPrincipal) userDetailsService.loadUserByUsername(username);
	        	if (!userLoginActivityCheck(userDetails)) {
	        		errMessage = AmigosConstants.INVALID_TOKEN+": No active session found!";
	        	}
	        	else {
		        	CurrentUser.setCurrentUserId(userDetails.getUserId());					// Setting current logged in user.
		        	
		        	if(jwtUtil.validateToken(jwt, userDetails, utilities.extractClientIp(request))) {
		        		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
		                        userDetails, null, userDetails.getAuthorities());
		                usernamePasswordAuthenticationToken
		                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		        	}
		        	else errMessage = AmigosConstants.INVALID_TOKEN;
	        	}
	        }
        } catch (JwtException e) {
			errMessage = AmigosConstants.INVALID_TOKEN+": "+e.getMessage();
		}
        if (!errMessage.isEmpty()) {
        	System.out.println(errMessage);
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        	response.getWriter().write(errMessage);
        }
        else
        	filterChain.doFilter(request, response);		
	}
	
	private boolean userLoginActivityCheck(UserPrincipal userDetails) {
		return userDetails.getActiveSessions() > 0;
	}

}
