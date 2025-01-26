package doktoree.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import doktoree.backend.domain.Employee;
import doktoree.backend.dtos.EmployeeDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.mapper.EmployeeMapper;
import doktoree.backend.repositories.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;
	
	@Autowired
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public Response<EmployeeDto> getEmployeeById(Long id) throws EntityNotExistingException {
		
		Optional<Employee> optionalEmployee = employeeRepository.findById(id);
		
		if(optionalEmployee.isPresent()) {
			EmployeeDto dto = EmployeeMapper.mapToEmployeeDto(optionalEmployee.get());
			Response<EmployeeDto> response = new Response<>();
			response.setDto(dto);
			response.setMessage("Employee found successfully!");;
			return response;
		}
			
		throw new EntityNotExistingException("Employee with given ID does not exists!");
	}

	@Override
	public Response<List<EmployeeDto>> getAllEmployees(int pageNumber) throws EmptyEntityListException {
		
		Page<Employee> employeesPage = employeeRepository.findAll(PageRequest.of(pageNumber, 10));
		List<Employee> employees = employeesPage.getContent();
		
		if(employees.isEmpty())
			throw new EmptyEntityListException("There are no reservations!");
		
		List<EmployeeDto> employeesDto = employees.stream().map(EmployeeMapper::mapToEmployeeDto).collect(Collectors.toList());
		Response<List<EmployeeDto>> response = new Response<>();
		response.setDto(employeesDto);
		response.setMessage("All employees successfully found!");
		
		
		return response;
	}
	
	

}
