package com.cesar.Jwt-Server.security.filter;

public class JwtTokenValidator extends OncePreRequestFilter {
	
	@Override
	protected void doFilterInternal(
					HttpServletRequest request, 
					HttpServletResponse response, 
					FilterChain filterChain) throws ServletException, IOException {
		
		String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if(jwtToken =! null){
			
			//Get token without Bearer prefix
			jwtToken = jwtToken.substring(7);
			
			//Validate token (break point here: if not valid, it will throw exception)
			DecodedJWT decodedToken = jwtUtils.validate(jwtToken);
			
			//If valid
			String username = jwtUtils.extractUsername(decodedToken);
			
			String authoritiesAsString = jwtUtils.getSpecificClaim(decodedToken, "authorities");
			Collection<? extends GrantedAuthority> authorities = 
						AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesAsString);
						
			//Authenticate
			SecurityContext context = SecurityContextHolder.getContext();
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
			context.setAuthentication(authentication);
			
			SecurityContextHolder.setContext(context);
		}
		//Continue filter chain
		filterChain.doFilter(request, response);
	}
	
	
	public JwtTokenValidator(JwtUtils jwtUtils){
		this.jwtUtils = jwtUtils;
	}
	
	private final JwtUtils jwtUtils;
}