package com.cesar.JwtServer.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("denyAll()")
@RequestMapping("/resource")
public class ResourceController{
	
	@GetMapping("/get")
	@PreAuthorize("permitAll()")
	public ResponseEntity<?> get(){
		return ResponseEntity.ok("GET");
	}
	
	@PostMapping("/post")
	@PreAuthorize("hasAuthority('WRITE')")
	public ResponseEntity<?> post(){
		return ResponseEntity.ok("POST");
	}
	
	@PutMapping("/put")
	@PreAuthorize("hasRole('DEV') or hasRole('ADMIN')")
	public ResponseEntity<?> put(){
		return ResponseEntity.ok("PUT");
	}
	
	@PatchMapping("/patch")
	public ResponseEntity<?> patch(){
		return ResponseEntity.ok("PATCH");
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> delete(){
		return ResponseEntity.ok("DELETE");
	}
}