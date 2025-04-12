package com.himansh.seamosamigos.config;

import com.himansh.seamosamigos.service.UserService;
import com.himansh.seamosamigos.constant.AmigosConstants;
import com.himansh.seamosamigos.utility.AmigosUtils;
import com.himansh.seamosamigos.utility.CurrentUser;
import com.himansh.seamosamigos.utility.JwtUtility;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	private final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);
	private final UserService userDetailsService;
	private final JwtUtility jwtUtil;
	private final AmigosUtils utilities;

	public JwtRequestFilter(UserService userDetailsService, JwtUtility jwtUtil, AmigosUtils utilities) {
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
		this.utilities = utilities;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String errMessage = "";
		try {
			String jwt = extractJwtFromRequest(request);
			if (jwt != null) {
				String username = jwtUtil.extractUsername(jwt);
				long loginId = jwtUtil.extractClaim(jwt, t -> t.get("loginId", Long.class));
				errMessage = processAuthentication(request, username, loginId, jwt);
			}
			if (!errMessage.isEmpty()) {
				log.error(errMessage);
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(errMessage);
			}
			filterChain.doFilter(request, response);
		} finally {
			CurrentUser.clear();
		}
	}

	private String extractJwtFromRequest(HttpServletRequest request) {
		final String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			log.info("Extracting JWT token.");
			return authorizationHeader.substring(7);
		}
		return null;
	}

	private String processAuthentication(HttpServletRequest request, String username, long loginId, String jwt) {
		log.info("Validating JWT token.");
		String errMessage = "";
		try {
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserPrincipal userDetails = (UserPrincipal) userDetailsService.loadUserByUsername(username);
				CurrentUser.setCurrentUserId(userDetails.getUserId()); 		// Setting current logged in user.
				int loginCounts = userDetailsService.getLoginDetails(loginId);
				if (loginCounts == 0) {
					errMessage = AmigosConstants.INVALID_TOKEN + ": No active session found!";
				} else {
					if (jwtUtil.validateToken(jwt, userDetails, utilities.extractClientIp(request))) {
						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						usernamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					} else {
						errMessage = AmigosConstants.INVALID_TOKEN;
					}
				}
			}
		} catch (JwtException e) {
			errMessage = AmigosConstants.INVALID_TOKEN + ": " + e.getMessage();
		}
		return errMessage;
	}
}
