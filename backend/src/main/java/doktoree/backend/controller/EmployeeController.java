package doktoree.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import doktoree.backend.dtos.EmployeeDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.services.EmployeeServiceImpl;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeController {

	private final EmployeeServiceImpl employeeService;
	
	@Autowired
	public EmployeeController(EmployeeServiceImpl employeeService) {
		this.employeeService = employeeService;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("{id}")
	public ResponseEntity<Response<EmployeeDto>> findEmployeeById(@PathVariable Long id){
		
		return ResponseEntity.ok(employeeService.getEmployeeById(id));
		
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<Response<List<EmployeeDto>>> getAllEmployees(@RequestParam(defaultValue = "0") int pageNumber){
		
		return ResponseEntity.ok(employeeService.getAllEmployees(pageNumber));
		
	}
	
}
