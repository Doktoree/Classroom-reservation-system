package doktoree.backend.services;

import java.util.List;

import doktoree.backend.dtos.EmployeeDto;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotExistingException;

public interface EmployeeService {

	public Response<EmployeeDto> getEmployeeById(Long id)
			throws EntityNotExistingException;
	
	public Response<List<EmployeeDto>> getAllEmployees(int pageNumber)
			throws EmptyEntityListException;
	
}
