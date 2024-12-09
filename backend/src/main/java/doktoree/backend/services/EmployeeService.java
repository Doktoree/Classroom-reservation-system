package doktoree.backend.services;

import doktoree.backend.dtos.EmployeeDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EntityNotExistingException;

public interface EmployeeService {

	public Response<EmployeeDto> getEmployeeById(Long id) throws EntityNotExistingException;
		
	
}
