

@RestController
public class Controller{
	
	@GetMapping("/free")
	public ResponseEntity<?> freeAccess(){
		return new ResponseEntity.ok("Free access");
	}
	
	@GetMapping("/secured")
	public ResponseEntity<?> securedAccess(){
		return ResponseEntity.ok("Open secured access");
	}
}