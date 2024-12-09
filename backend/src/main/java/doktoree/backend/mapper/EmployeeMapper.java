package doktoree.backend.mapper;

import doktoree.backend.domain.Department;
import doktoree.backend.domain.Employee;
import doktoree.backend.dtos.EmployeeDto;

public class EmployeeMapper {

	public static Employee mapToEmployee(EmployeeDto dto) {

		Department department = new Department();
		department.setId(dto.departmentId());
		
		return new Employee(dto.id(),
				dto.name(), 
				dto.lastName(), 
				dto.academicRank(), 
				dto.title(),
				department);
	}
	
	public static EmployeeDto mapToEmployeeDto(Employee e) {
		
		return new EmployeeDto(
				e.getId(), 
				e.getName(),
				e.getLastName(), 
				e.getAcademicRank(), 
				e.getTitle(),
				e.getDepartment().getId());
		
	}
}
