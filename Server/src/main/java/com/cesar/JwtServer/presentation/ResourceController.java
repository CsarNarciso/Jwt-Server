package com.cesar.JwtServer.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resource")
public class ResourceController{
	
	@GetMapping
	public ResponseEntity<?> get(){
		return ResponseEntity.ok("GET");
	}
	
	@PostMapping
	public ResponseEntity<?> post(){
		return ResponseEntity.ok("POST");
	}
	
	@PutMapping
	public ResponseEntity<?> put(){
		return ResponseEntity.ok("PUT");
	}
	
	@PatchMapping
	public ResponseEntity<?> patch(){
		return ResponseEntity.ok("PATCH");
	}
	
	@DeleteMapping
	public ResponseEntity<?> delete(){
		return ResponseEntity.ok("DELETE");
	}
}