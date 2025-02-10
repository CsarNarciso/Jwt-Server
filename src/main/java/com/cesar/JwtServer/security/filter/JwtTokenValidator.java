package com.cesar.JwtServer.security.filter;

import java.io.IOException;
import java.util.Collection;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cesar.JwtServer.util.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (jwtToken != null) {

			// Get token without Bearer prefix
			jwtToken = jwtToken.substring(7);

			// Validate token (break point here: if not valid, it will throw exception)
			DecodedJWT decodedToken = jwtUtils.validateToken(jwtToken);

			// If valid
			String username = jwtUtils.extractUsername(decodedToken);

			String authoritiesAsString = jwtUtils.getSpecificClaim(decodedToken, "authorities").asString();
			Collection<? extends GrantedAuthority> authorities = AuthorityUtils
					.commaSeparatedStringToAuthorityList(authoritiesAsString);

			// Authenticate
			SecurityContext context = SecurityContextHolder.getContext();

			Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
			context.setAuthentication(authentication);

			SecurityContextHolder.setContext(context);
		}
		// Continue filter chain
		filterChain.doFilter(request, response);
	}

	public JwtTokenValidator(JwtUtils jwtUtils) {
		this.jwtUtils = jwtUtils;
	}

	private final JwtUtils jwtUtils;
}