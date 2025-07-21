package doktoree.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import doktoree.backend.dtos.EmployeeDto;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.services.EmployeeServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee/")
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeController {

	private final EmployeeServiceImpl employeeService;
	
	@Autowired
	public EmployeeController(EmployeeServiceImpl employeeService) {
		this.employeeService = employeeService;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("{id}")
	public ResponseEntity<Response<EmployeeDto>> findEmployeeById(@PathVariable Long id) {
		
		return ResponseEntity.ok(employeeService.getEmployeeById(id));
		
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<Response<List<EmployeeDto>>> getAllEmployees(
			@RequestParam(defaultValue = "0") int pageNumber
	) {
		
		return ResponseEntity.ok(employeeService.getAllEmployees(pageNumber));
		
	}
	
}
