

@RestController
public class Controller{
	
	@GetMapping("/get")
	public ResponseEntity<?> get(){
		return new ResponseEntity.ok("GET");
	}
	
	@PostMapping("/post")
	public ResponseEntity<?> post(){
		return ResponseEntity.ok("POST");
	}
	
	@PutMapping("/put")
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