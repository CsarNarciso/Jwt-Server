package com.cesar.Jwt-Server.presentation;

@RestController
@PreAuthorize("denyAll()")
public class Controller{
	
	@GetMapping("/get")
	@PreAuthorize("permitAll()")
	public ResponseEntity<?> get(){
		return new ResponseEntity.ok("GET");
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