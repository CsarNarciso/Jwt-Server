package com.cesar.JwtServer.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.cesar.JwtServer.exception.NoAuthenticatedException;
import com.cesar.JwtServer.persistence.entity.JwtTokenType;
import jakarta.servlet.http.Cookie;
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

	private final List<String> EXCLUDED_PATHS =List.of(
			"/auth/login",
			"/auth/register",
			"/auth/refresh",
			"/h2-console");
	private final JwtUtils jwtUtils;

	public JwtTokenValidator(JwtUtils jwtUtils) {
		this.jwtUtils = jwtUtils;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Do not filter for not allowed paths
		if(isExcluded(request.getServletPath())){
			filterChain.doFilter(request, response);
			return;
		}

		Cookie[] cookies = request.getCookies();
		if(cookies.length > 0) {

			Cookie tokenCookie = Arrays.stream(cookies).filter(c -> c.getName().equals("token"))
					.findFirst().orElseThrow(() -> new NoAuthenticatedException("Missing access token"));

			String jwtToken = tokenCookie.getValue();

			// Validate token (break point here: if not valid, it will throw exception)
			DecodedJWT decodedToken = jwtUtils.validateToken(jwtToken, JwtTokenType.ACCESS);

			// If valid
			String username = jwtUtils.extractUsername(decodedToken);

			// Get authorities
			String authoritiesAsString = jwtUtils.getSpecificClaim(decodedToken, "authorities").asString();
			Collection<? extends GrantedAuthority> authorities = AuthorityUtils
					.commaSeparatedStringToAuthorityList(authoritiesAsString);

			// Authenticate
			SecurityContext context = SecurityContextHolder.getContext();

			Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
			context.setAuthentication(authentication);

			SecurityContextHolder.setContext(context);

			// Continue filter chain
			filterChain.doFilter(request, response);
			return;
		}
		throw new NoAuthenticatedException("Missing access token");
	}


	private boolean isExcluded(String path){
		return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
	}
}