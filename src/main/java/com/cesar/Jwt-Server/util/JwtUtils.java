package com.cesar.Jwt-Server.util;

@Component
public class JwtUtils{
	
	public String createToken(Authentication authentication){
		
		//Get current authenticated user username
		String username = authentication.getPrinciapl().toString();
		
		//Get all authorities (permissions and roles)
		String authorities = authentication.getAuthorities()
					.stream()
					.map(GrantedAuthority::getAuthority)
					.collect(Collectors.joining(","));
		
		//Generate JWT token
		return JWT.create()
			.withIssuer(issuer)
			.withSubject(username)
			.withClaim("authorities", autorithies)
			.withIssuedAt(new Date())
			.withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
			.withNotBefore(new Date(System.currentTimeMillis()))
			.withJWTId(UUID.randomUUID().toString())
			.sign(algorithm);
	}
	
	public void validToken(DecodedJwt decodedJwt){
		
	}
	
	public void getSpecificClaim(String clainName){
		
	}
	
	public void getAllClaims(){
		
	}
	
	
	
	
	public JwtUtils(){
		this.algorithm = Algorithm.HMAC256(secret);
	}
	
	
	@Value("${jwt.secret}")
	private final String secret;
	
	@Value("${jwt.issuer}")
	private final String issuer;
	
	private final Algorithm algorithm;
}