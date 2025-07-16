package com.cesar.JwtServer.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/resource")
public class ResourceController{
	
	@GetMapping
	public ResponseEntity<?> get(){
		return ResponseEntity.ok(Map.of("message", "GET"));
	}
	
	@PostMapping
	public ResponseEntity<?> post(){
		return ResponseEntity.ok(Map.of("message", "POST"));
	}
	
	@PutMapping
	public ResponseEntity<?> put(){
		return ResponseEntity.ok(Map.of("message", "PUT"));
	}
	
	@PatchMapping
	public ResponseEntity<?> patch(){
		return ResponseEntity.ok(Map.of("message", "PATCH"));
	}
	
	@DeleteMapping
	public ResponseEntity<?> delete(){
		return ResponseEntity.ok(Map.of("message", "DELETE"));
	}
}