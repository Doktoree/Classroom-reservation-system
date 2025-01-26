package doktoree.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import doktoree.backend.dtos.UserDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.services.UserServiceImpl;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("http://localhost:5173/")
public class UserController {

	private final UserServiceImpl userService;
	
	@Autowired
	public UserController(UserServiceImpl userService) {
		super();
		this.userService = userService;
	}

	@GetMapping("{id}")
	public ResponseEntity<Response<UserDto>> findUserById(@PathVariable Long id){
		
		return ResponseEntity.ok(userService.findUserById(id));
		
		
	}
	
	@PostMapping
	public ResponseEntity<Response<UserDto>> saveUser(@RequestBody UserDto dto){
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(dto));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Response<UserDto>> deleteUser(@PathVariable Long id){
		
		return ResponseEntity.ok(userService.deleteUser(id));
		
	}
	
	@GetMapping("/all")
	public ResponseEntity<Response<List<UserDto>>> getAllUsers(@RequestParam(defaultValue = "0") int pageNumber){
		
		return ResponseEntity.ok(userService.getAllUsers(pageNumber));
		
	}
	
	@PatchMapping
	public ResponseEntity<Response<UserDto>> updateUser(@RequestBody UserDto dto){
		
		return ResponseEntity.ok(userService.updateUser(dto));
	
}
}
