package com.cesar.JwtServer.util;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtUtils{
	
	public String createToken(Authentication authentication){
		
		//Get current authenticated user username
		String username = authentication.getPrincipal().toString();
		
		//Get all authorities (permissions and roles)
		String authorities = authentication.getAuthorities()
					.stream()
					.map(GrantedAuthority::getAuthority)
					.collect(Collectors.joining(","));
		
		//Generate JWT token
		return JWT.create()
			.withIssuer(issuer)
			.withSubject(username)
			.withClaim("authorities", authorities)
			.withIssuedAt(new Date())
			.withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
			.withNotBefore(new Date(System.currentTimeMillis()))
			.withJWTId(UUID.randomUUID().toString())
			.sign(algorithm);
	}
	
	public DecodedJWT validateToken(String token){
		
		try{
			
			JWTVerifier verifier = JWT.require(algorithm)
							.withIssuer(issuer)
							.build();
							
			//Get decoded Jwt token if successful veirfication
			return verifier.verify(token);
			
		} catch(JWTVerificationException ex){
			throw new JWTVerificationException("Token invalid. No authorization.");
		}
	}
	
	public String extractUsername(DecodedJWT decodedToken){
		return decodedToken.getSubject().toString();
	}
	
	public Claim getSpecificClaim(DecodedJWT decodedToken, String clainName){
		return decodedToken.getClaim(clainName);
	}
	
	public Map<String, Claim> getAllClaims(DecodedJWT decodedToken){
		return decodedToken.getClaims();
	}
	
	
	
	
	public JwtUtils(){
		this.algorithm = Algorithm.HMAC256(secret);
	}
	
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.issuer}")
	private String issuer;
	
	private final Algorithm algorithm;
}