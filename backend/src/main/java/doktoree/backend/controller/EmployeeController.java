package doktoree.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import doktoree.backend.dtos.EmployeeDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.services.EmployeeServiceImpl;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

	private final EmployeeServiceImpl employeeService;
	
	@Autowired
	public EmployeeController(EmployeeServiceImpl employeeService) {
		this.employeeService = employeeService;
	}


	@GetMapping("{id}")
	public ResponseEntity<Response<EmployeeDto>> findEmployeeById(@PathVariable Long id){
		
		return ResponseEntity.ok(employeeService.getEmployeeById(id));
		
	}
	
	@GetMapping
	public ResponseEntity<Response<List<EmployeeDto>>> getAllEmployees(@RequestParam(defaultValue = "0") int pageNumber){
		
		return ResponseEntity.ok(employeeService.getAllEmployees(pageNumber));
		
	}
	
}
