package doktoree.backend.dtos;

import doktoree.backend.domain.Department;
import doktoree.backend.enums.AcademicRank;
import doktoree.backend.enums.Title;

public record EmployeeDto(
		Long id, 
		String name, 
		String lastName, 
		AcademicRank academicRank, 
		Title title, 
		Department department) {

}
