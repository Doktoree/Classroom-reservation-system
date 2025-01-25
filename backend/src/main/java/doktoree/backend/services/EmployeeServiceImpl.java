package doktoree.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import doktoree.backend.domain.Employee;
import doktoree.backend.dtos.EmployeeDto;
import doktoree.backend.error_response.Response;
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
	
	

}
